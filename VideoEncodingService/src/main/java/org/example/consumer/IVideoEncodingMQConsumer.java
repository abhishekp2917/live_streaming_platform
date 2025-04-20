package org.example.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface IVideoEncodingMQConsumer<T> {

    void listen(T messageObj, Channel channel, Message message) throws Exception;
}
