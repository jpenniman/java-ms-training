package com.northwind.customerservice.api;

import com.northwind.customerservice.domain.Address;

class AddressMapper {
    public static AddressModel toModel(Address entity) {
        AddressModel model = new AddressModel();
        model.setId(entity.getId());
        model.setCity(entity.getCity());
        model.setCountry(entity.getCountry());
        model.setDefaultBilling(entity.isDefaultBilling());
        model.setDefaultShipping(entity.isDefaultShipping());
        model.setPostalCode(entity.getPostalCode());
        model.setStateOrProvince(entity.getStateOrProvince());
        model.setStreetOrPoBox(entity.getStreetOrPoBox());
        model.setVersion(entity.getVersion());

        return model;
    }

    public static Address toEntity(AddressModel model) {
        Address entity = new Address(model.getStreetOrPoBox(), model.getPostalCode());
        entity.setId(model.getId());
        entity.setCity(model.getCity());
        entity.setCountry(model.getCountry());
        entity.setDefaultBilling(model.isDefaultBilling());
        entity.setDefaultShipping(model.isDefaultShipping());
        entity.setStateOrProvince(model.getStateOrProvince());
        entity.setVersion(model.getVersion());

        return entity;
    }

}
