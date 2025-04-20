package org.example.consumer;

import org.example.chain.IVideoEncodingProcessChain;
import org.example.chain.context.FFmpegVideoEncodingContextImpl;
import org.example.chain.context.IVideoEncodingContext;
import org.example.enums.VideoEncodingStatus;
import org.example.persistence.IVideoDbService;
import org.example.pojo.VideoEncodingMessage;
import org.example.service.ICleanupService;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

@Service
public class RabbitVideoEncodingMQConsumerImpl implements IVideoEncodingMQConsumer <VideoEncodingMessage> {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ICleanupService cleanupService;
    @Autowired
    private IVideoDbService videoDbService;

    @Autowired
    private IVideoEncodingProcessChain videoEncodingProcessChain;

    @RabbitListener(
            id = "${video.encoding.queue.name}",
            ackMode = "${video.encoding.ackMode}",
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "${video.encoding.queue.name}",
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = "${video.encoding.queue.type}")
                            }),
                    exchange = @Exchange(
                            value = "${video.encoding.exchange.name}",
                            type = "${video.encoding.exchange.type}",
                            durable = "true",
                            autoDelete = "false"
                    ),
                    key = "${video.encoding.routing.key}"
            )
    )
    @Transactional
    public void listen(VideoEncodingMessage videoEncodingMessage, Channel channel, Message message) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{
            IVideoEncodingContext context = FFmpegVideoEncodingContextImpl.builder()
                    .videoEncodingMessage(videoEncodingMessage)
                    .build();
            videoDbService.updateEncodingStatus(videoEncodingMessage.getVideoId(), VideoEncodingStatus.PROCESSING);
            videoEncodingProcessChain.doProcess(context);
            videoDbService.updateEncodingStatus(videoEncodingMessage.getVideoId(), VideoEncodingStatus.SUCCESS);
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception e) {
            videoDbService.updateEncodingStatus(videoEncodingMessage.getVideoId(), VideoEncodingStatus.FAILED);
            channel.basicNack(deliveryTag, false, true);
            throw new RuntimeException(e.getMessage());
        }
        finally {
            if(videoEncodingMessage!=null) {
                cleanupService.cleanUp(videoEncodingMessage);
            }
        }
    }
}

