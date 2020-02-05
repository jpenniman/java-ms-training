package com.northwind.orderservice.api;

import com.northwind.orderservice.domain.OrderItem;

public class OrderItemMapper {

    public static OrderItemModel toModel(OrderItem entity) {
        OrderItemModel model = new OrderItemModel();
        model.setDiscount(entity.getDiscount());
        model.setProductId(entity.getId().getProductId());
        model.setProductName(entity.getProductName());
        model.setQuantity(entity.getQuantity());
        model.setQuantityPerUnit(entity.getQuantityPerUnit());
        model.setUnitPrice(entity.getUnitPrice());
        model.setVersion(entity.getVersion());
        return model;
    }
}
