package org.example.producer;

import org.example.pojo.VideoEncodingMessage;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class RabbitVideoEncodingMQProducerImpl implements IVideoEncodingMQProducer<VideoEncodingMessage> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${video.encoding.exchange.name}")
    private String exchangeName;

    @Value("${video.encoding.routing.key}")
    private String routingKey;

    @Override
    public boolean publish(VideoEncodingMessage message) {
        try{
            MessagePostProcessor messagePostProcessor = videoEncodingMessage -> {
                videoEncodingMessage.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                videoEncodingMessage.getMessageProperties().setPriority(1);
                videoEncodingMessage.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
                videoEncodingMessage.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                return videoEncodingMessage;
            };
            rabbitTemplate.convertAndSend(this.exchangeName, this.routingKey, message, messagePostProcessor);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
