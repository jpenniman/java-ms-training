package com.northwind.orderservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.orderservice.adapters.ShippingServiceClient;
import com.northwind.orderservice.domain.Order;
import com.northwind.orderservice.domain.OrderEvent;
import com.northwind.orderservice.infrastructure.LoggerFactory;
import com.northwind.orderservice.repositories.OrderRepository;
import org.apache.commons.logging.Log;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository repository;
    private ShippingServiceClient shippingClient;
    private RabbitTemplate rabbitTemplate;
    private Log logger;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public OrderService(OrderRepository repository,
                        ShippingServiceClient shippingClient,
                        RabbitTemplate rabbitTemplate,
                        LoggerFactory loggerFactory) {
        this.repository = repository;
        this.shippingClient = shippingClient;
        this.rabbitTemplate = rabbitTemplate;
        this.logger = loggerFactory.getLogger(OrderService.class);
    }

    public List<Order> getAll(int offset, int limit) {
        int page = 1;
        if (offset > 0)
            page = (offset/limit) + 1;

        return repository.findAll(PageRequest.of(page, limit)).toList();
    }

    public List<Order> getByCustomerId(long customerId, int offset, int limit) {
        int page = 1;
        if (offset > 0)
            page = (offset/limit) + 1;

        return repository.findByCustomerId(customerId, PageRequest.of(page, limit)).toList();
    }

    public List<Order> getByCustomerOrderHistory(long customerId, Date startDate, Date endDate, int offset, int limit) {
        int page = 1;
        if (offset > 0)
            page = (offset/limit) + 1;

        List<Order> results =  repository.findByCustomerIdAndOrderDateBetween(customerId, startDate, endDate);

        return results;
    }

    public Optional<Order> get(long id) {
        return repository.findById(id);
    }

    @Transactional
    public Order save(Order entity) {
        boolean isNew = false;
        if (entity.getId() == 0){
            isNew = true;
            double freight = shippingClient.getFreightAmount(entity.getShipCountry());
            entity.setFreight(BigDecimal.valueOf(freight));
        }

        repository.saveAndFlush(entity);

        try {
            OrderEvent event = new OrderEvent();
            event.getData().put("order", objectMapper.writeValueAsString(entity));
            if (isNew) {
                // OrderCreated event
                event.setEventType("OrderPlaced");
            } else {
                //OrderUpdated event
                event.setEventType("OrderUpdated");
            }
            rabbitTemplate.convertAndSend("order-service", "", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            logger.debug("An error occurred serializing Order for event.", e);
        }
        return entity;
    }

    public void delete(long id) {
        repository.deleteById(id);
        repository.flush();
    }
}
