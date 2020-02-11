package com.northwind.catalogservice.api;

import com.northwind.catalogservice.domain.Product;

public class ProductMapper {
    public static ProductModel toModel(Product entity) {
        ProductModel model = new ProductModel();
        model.setCategory(CategoryMapper.toModel(entity.getCategory()));
        model.setId(entity.getId());
        model.setProductName(entity.getProductName());
        model.setListPrice(entity.getListPrice());
        model.setQuantityPerUnit(entity.getQuantityPerUnit());
        model.setPublished(entity.isPublished());
        model.setVersion(entity.getVersion());
        return model;
    }

    public static Product newProduct(ProductModel model) {
        Product entity = new Product(model.getProductName());
        entity.setListPrice(model.getListPrice());
        entity.setQuantityPerUnit(model.getQuantityPerUnit());
        entity.setPublished(model.isPublished());
        return entity;
    }

    public static Product merge(ProductModel model, Product entity) {
        entity.setProductName(model.getProductName());
        entity.setListPrice(model.getListPrice());
        entity.setQuantityPerUnit(model.getQuantityPerUnit());
        entity.setPublished(model.isPublished());
        return entity;
    }
}
