package com.northwind.shippingservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.shippingservice.domain.ShippingEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class ShippingService {
    private RabbitTemplate rabbit;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ShippingService(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void notifyOrderShipped(long orderId) throws JsonProcessingException {
        ShippingEvent event = new ShippingEvent();
        event.setEventType("OrderShipped");
        event.getData().put("orderNo", orderId);
        event.getData().put("shippedDate", Calendar.getInstance().getTime());

        String json = objectMapper.writeValueAsString(event);
        rabbit.convertAndSend("shipping-events", "", json);
    }
}
