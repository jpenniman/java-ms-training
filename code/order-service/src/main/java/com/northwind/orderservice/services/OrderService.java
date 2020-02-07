package com.northwind.orderservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.northwind.orderservice.adapters.ShippingServiceClient;
import com.northwind.orderservice.domain.Order;
import com.northwind.orderservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository repository;
    private ShippingServiceClient shippingClient;
    @Autowired
    public OrderService(OrderRepository repository, ShippingServiceClient shippingClient) {
        this.repository = repository;
        this.shippingClient = shippingClient;
    }

    public List<Order> getAll(int offset, int limit) {
        int page = 1;
        if (offset > 0)
            page = (offset/limit) + 1;

        return repository.findAll(PageRequest.of(page, limit)).toList();
    }

    public Optional<Order> get(long id) {
        return repository.findById(id);
    }

    public Order save(Order entity) {
        double freight = shippingClient.getFreightAmount(entity.getShipCountry());
        entity.setFreight(BigDecimal.valueOf(freight));
        return repository.saveAndFlush(entity);
    }

    public void delete(long id) {
        repository.deleteById(id);
        repository.flush();
    }
}
