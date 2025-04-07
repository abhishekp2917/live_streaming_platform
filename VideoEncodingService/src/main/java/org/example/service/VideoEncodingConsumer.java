package org.example.service;

import org.example.model.VideoEncodingMessage;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoEncodingConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RabbitListener(
            id = "${video.encoding.queue.name}",
            ackMode = "${video.encoding.ackMode}",
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "${video.encoding.queue.name}",
                            durable = "true",
                            exclusive = "true",
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

                    key = "${video.encoding.routing.keys}"
            )
    )
    public void listenVideoEncodingQueue(VideoEncodingMessage videoEncodingMessage) {
        System.out.println(videoEncodingMessage);
    }

}

