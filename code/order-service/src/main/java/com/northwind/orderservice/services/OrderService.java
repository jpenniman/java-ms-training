package com.northwind.orderservice.services;

import com.northwind.orderservice.domain.Order;
import com.northwind.orderservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository repository;

    @Autowired
    public OrderService(OrderRepository repository) {
        this.repository = repository;
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
}
