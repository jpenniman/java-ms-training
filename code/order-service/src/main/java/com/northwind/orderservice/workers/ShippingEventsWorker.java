package com.northwind.orderservice.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.orderservice.domain.Order;
import com.northwind.orderservice.services.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShippingEventsWorker {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Log logger;
    private OrderService service;

    public ShippingEventsWorker( OrderService service) {
        this.logger = LogFactory.getLog(ShippingEventsWorker.class);
        this.service = service;
    }

    @RabbitListener(queues = "shipping-events-orders-service")
    public void receiveEvent(String message) {
        try {
            ShippingEvent event = objectMapper.readValue(message, ShippingEvent.class);

            Order order = service.get(Long.parseLong(event.getData().get("orderNo").toString())).get();
            order.orderShipped(new Date(Long.parseLong(event.getData().get("shippedDate").toString())), 0);

            service.save(order);
        } catch (JsonProcessingException e) {
            logger.warn(String.format("Unable to process event from shipping: %s", message), e);
        }
    }
}
