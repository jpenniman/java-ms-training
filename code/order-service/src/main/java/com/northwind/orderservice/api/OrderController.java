package com.northwind.orderservice.api;

import com.northwind.orderservice.domain.Order;
import com.northwind.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderModel>> get(
            @RequestParam(required = false)Optional<Integer> offset,
            @RequestParam(required = false)Optional<Integer> limit) {

        List<OrderModel> orders = service.getAll(offset.orElse(0), limit.orElse(10))
                .stream().map(o->OrderMapper.toModel(o))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderNo}")
    public ResponseEntity<OrderModel> get(@PathVariable long orderNo) {
        Optional<Order> order = service.get(orderNo);
        if (!order.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(OrderMapper.toModel(order.get()));
    }
}
