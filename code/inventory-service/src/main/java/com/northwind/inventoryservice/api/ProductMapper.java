package com.northwind.inventoryservice.api;

import com.northwind.inventoryservice.domain.Product;

public class ProductMapper {

    public static ProductModel toModel(Product entity) {
        ProductModel model = new ProductModel();
        model.setId(entity.getId());
        model.setDiscontinued(entity.isDiscontinued());
        model.setLocation(entity.getLocation());
        model.setProductName(entity.getProductName());
        model.setQuantityPerUnit(entity.getQuantityPerUnit());
        model.setReorderLevel(entity.getReorderLevel());
        model.setUnitPrice(entity.getUnitPrice());
        model.setUnitsInStock(entity.getUnitsInStock());
        model.setUnitsOnOrder(entity.getUnitsOnOrder());
        model.setVersion(entity.getVersion());
        return model;
    }

    public static Product newEntity(ProductModel model) {
        Product entity = new Product(model.getProductName());
        entity.setDiscontinued(model.isDiscontinued());
        entity.setLocation(model.getLocation());
        entity.setProductName(model.getProductName());
        entity.setQuantityPerUnit(model.getQuantityPerUnit());
        entity.setReorderLevel(model.getReorderLevel());
        entity.setUnitPrice(model.getUnitPrice());
        entity.setUnitsInStock(model.getUnitsInStock());
        entity.setUnitsOnOrder(model.getUnitsOnOrder());
        return entity;
    }

    public static Product merge(ProductModel model, Product entity) {
        entity.setProductName(model.getProductName());
        entity.setDiscontinued(model.isDiscontinued());
        entity.setLocation(model.getLocation());
        entity.setProductName(model.getProductName());
        entity.setQuantityPerUnit(model.getQuantityPerUnit());
        entity.setReorderLevel(model.getReorderLevel());
        entity.setUnitPrice(model.getUnitPrice());
        entity.setUnitsInStock(model.getUnitsInStock());
        entity.setUnitsOnOrder(model.getUnitsOnOrder());
        return entity;
    }
}
