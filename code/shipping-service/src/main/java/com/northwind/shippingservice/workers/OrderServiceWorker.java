package com.northwind.shippingservice.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.shippingservice.services.ShippingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceWorker {

    ObjectMapper objectMapper = new ObjectMapper();

    private ShippingService service;

    public OrderServiceWorker(ShippingService service) {
        this.service = service;
    }

    @RabbitListener(queues = "order-service-shipping-service")
    public void processMessage(String message) {

        try {
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);

            if (event.getEventType()=="OrderPlaced") {
                String payload = event.getData().get("order").toString();
                OrderModel order = objectMapper.readValue(payload, OrderModel.class);

                PackingSlip packingSlip = new PackingSlip();
                packingSlip.setCustomerName(order.getCompanyName());
;                service.save(packingSlip);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
