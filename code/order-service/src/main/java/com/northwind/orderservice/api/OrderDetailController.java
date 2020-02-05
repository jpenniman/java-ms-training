package com.northwind.orderservice.api;

import com.northwind.orderservice.domain.Order;
import com.northwind.orderservice.domain.OrderItem;
import com.northwind.orderservice.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders/{orderNo}/products")
public class OrderDetailController {

    private OrderService service;

    public OrderDetailController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderItemModel>> get(@PathVariable long orderNo) {
        Optional<Order> order = service.get(orderNo);
        if (!order.isPresent())
            return ResponseEntity.notFound().build();

        List<OrderItemModel> items = new ArrayList<>(order.get().getItems()).stream()
                .map(i->OrderItemMapper.toModel(i))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<OrderItemModel> get(@PathVariable long orderNo, @PathVariable long productId) {
        Optional<Order> order = service.get(orderNo);
        if (!order.isPresent())
            return ResponseEntity.notFound().build();

        List<OrderItem> items = new ArrayList<>(order.get().getItems());
        Optional<OrderItem> item = items.stream().filter(i->i.getId().getProductId() == productId)
                .findFirst();

        if(!item.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(OrderItemMapper.toModel(item.get()));
    }
}
