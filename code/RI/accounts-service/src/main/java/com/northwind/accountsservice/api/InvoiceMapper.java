package com.northwind.accountsservice.api;

import com.northwind.accountsservice.domain.Invoice;

final class InvoiceMapper {
    private InvoiceMapper() {}

    public static InvoiceModel toModel(Invoice entity) {
        InvoiceModel model = new InvoiceModel();
        model.setInvoiceNo(entity.getId());
        model.setCustomerNo(entity.getCustomerNo());
        model.setCustomerName(entity.getCustomerName());
        model.setInvoiceDate(entity.getInvoiceDate());
        model.setFreight(entity.getFreight());
        model.setOrderNo(entity.getOrderNo());
        model.setPaid(entity.isPaid());
        model.setSubtotal(entity.getSubtotal());
        model.setTotal(entity.getTotal());
        model.setVersion(entity.getVersion());
        return model;
    }
}
