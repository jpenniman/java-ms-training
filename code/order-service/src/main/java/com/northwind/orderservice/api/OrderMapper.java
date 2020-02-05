package com.northwind.orderservice.api;

import com.northwind.orderservice.domain.Order;

public class OrderMapper {
    public static OrderModel toModel(Order entity) {
        OrderModel model = new OrderModel();
        model.setOrderNo(entity.getId());
        model.setCustomerNo(entity.getCustomerNo());
        model.setCompanyName(entity.getCompanyName());
        model.setOrderDate(entity.getOrderDate());
        model.setTotal(entity.getOrderTotal());
        return model;
    }
}
