package com.northwind.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.loggingservice.providers.LoggingEvent;
import com.northwind.loggingservice.providers.LoggingProvider;
import com.northwind.loggingservice.providers.LoggingProviderException;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class LoggingWorker implements Runnable {

    private LoggingProvider loggingProvider;
    private Connection cn;
    private Channel channel;
    private ObjectMapper objectMapper;
    private BlockingQueue<Delivery> queue;
    Timer flushTimer;

    private int bufferSize = 5;

    public LoggingWorker(LoggingProvider loggingProvider) {
        this.loggingProvider = loggingProvider;
        objectMapper = new ObjectMapper();

        queue = new ArrayBlockingQueue<>(bufferSize);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setHost("localhost");

        try {
            cn = factory.newConnection();
            channel = cn.createChannel();
            channel.basicQos(bufferSize);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        TimerTask flushBufferTask = new TimerTask() {
            public void run() {
                if (!queue.isEmpty())
                    sendBatch();
            }
        };
        flushTimer = new Timer();
        flushTimer.scheduleAtFixedRate(flushBufferTask, 0, 10000);
    }

    @Override
    public void run() {
        try {
            channel.basicConsume("logging-service", false, this::processMessage, consumerTag -> { });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String consumerTag, Delivery delivery) {
        while (!queue.offer(delivery)) {
            sendBatch();
        }
    }

    private void sendBatch() {
        List<Delivery> batch = new ArrayList<>();
        queue.drainTo(batch);

        List<LoggingEvent> events = new ArrayList<>();

        for(Delivery delivery : batch) {
            String json = new String(delivery.getBody());

            LoggingEvent event = null;
            try {
                event = objectMapper.readValue(json, LoggingEvent.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (event != null) {
                events.add(event);
            }
        }

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
