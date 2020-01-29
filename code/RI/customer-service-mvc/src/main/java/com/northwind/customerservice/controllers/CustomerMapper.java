package com.northwind.customerservice.mappers;

public class CustomerMapper {
    public com.northwind.customerservice.domain.Customer toDomain(com.northwind.customerservice.models.Customer model) {
        com.northwind.customerservice.domain.Customer entity = new com.northwind.customerservice.domain.Customer();
        entity.setName(model.getName());
        return entity;
    }

    public com.northwind.customerservice.models.Customer toModel(com.northwind.customerservice.domain.Customer enity) {
        com.northwind.customerservice.models.Customer model = new com.northwind.customerservice.models.Customer();
        model.setName(enity.getName());
        return model;
    }
}
