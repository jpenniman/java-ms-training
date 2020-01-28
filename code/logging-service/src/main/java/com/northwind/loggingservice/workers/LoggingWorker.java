package com.northwind.loggingservice.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.loggingservice.providers.LoggingEvent;
import com.northwind.loggingservice.providers.LoggingProvider;
import com.northwind.loggingservice.providers.LoggingProviderException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class LoggingWorker implements Runnable {

    private LoggingProvider loggingProvider;
    private Connection cn;
    private Channel channel;
    private ObjectMapper objectMapper;

    private Subject<Delivery> messages;

    private int bufferSize = 5;

    public LoggingWorker(LoggingProvider loggingProvider) {
        this.loggingProvider = loggingProvider;
        objectMapper = new ObjectMapper();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setHost("localhost");

        try {
            cn = factory.newConnection();
            channel = cn.createChannel();
            channel.basicQos(bufferSize);

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        messages = PublishSubject.create();

        messages.buffer(10, TimeUnit.SECONDS, bufferSize)
                .subscribe(this::sendBatch);

        try {
            channel.basicConsume("logging-service", false, this::processMessage, consumerTag -> { });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String consumerTag, Delivery delivery) {
        messages.onNext(delivery);
    }

    private void sendBatch(List<Delivery> batch) {

        if (batch.size() == 0)
            return;

        List<LoggingEvent> events = batch.stream().map(delivery->{
            String json = new String(delivery.getBody());
            try {
                return objectMapper.readValue(json, LoggingEvent.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        long deliveryTag = batch.get(batch.size() -1).getEnvelope().getDeliveryTag();
        try {
            loggingProvider.sendEvent(events);

            try {
                channel.basicAck(deliveryTag, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (LoggingProviderException ex) {
            try {
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
