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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private LoggingServiceConfig serviceConfig;
    private QueueConfig queueConfig;

    private Logger internalLogger;

    public LoggingWorker(LoggingProvider loggingProvider,
                         LoggingServiceConfig serviceConfig,
                         QueueConfig queueConfig) {
        this.loggingProvider = loggingProvider;
        this.serviceConfig = serviceConfig;
        this.queueConfig = queueConfig;

        this.bufferSize = serviceConfig.getBufferSize();

        internalLogger = LoggerFactory.getLogger(LoggingWorker.class);
        objectMapper = new ObjectMapper();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(queueConfig.getUsername());
        factory.setPassword(queueConfig.getPassword());
        factory.setHost(queueConfig.getServer());

        try {
            cn = factory.newConnection();
            channel = cn.createChannel();
            channel.basicQos(bufferSize);

        } catch (IOException | TimeoutException e) {
            internalLogger.debug("Error creating connection to rabbitmq", e);
        }
    }

    @Override
    public void run() {
        messages = PublishSubject.create();

        messages.buffer(serviceConfig.getFlushIntervalInSeconds(), TimeUnit.SECONDS, bufferSize)
                .subscribe(this::sendBatch);

        try {
            channel.basicConsume(queueConfig.getQueuename(), false, this::processMessage, consumerTag -> { });
        } catch (IOException e) {
            internalLogger.debug("Error sending starting consumer", e);
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
                internalLogger.debug("Error deserializing message", e);
                return null;
            }
        }).collect(Collectors.toList());

        long deliveryTag = batch.get(batch.size() -1).getEnvelope().getDeliveryTag();

        try {
            loggingProvider.sendEvent(events);

            try {
                channel.basicAck(deliveryTag, true);
            } catch (IOException e) {
                internalLogger.debug("Error sending ACK", e);
            }
        } catch (LoggingProviderException ex) {
            internalLogger.debug("Error sending to logging provider", ex);
            try {
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException e) {
                internalLogger.debug("Error sending NACK", e);
            }
        }
    }
}
