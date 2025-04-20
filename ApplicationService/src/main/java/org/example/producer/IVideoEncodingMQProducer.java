package org.example.producer;


public interface IVideoEncodingMQProducer<T> {

    boolean publish(T message);
}
