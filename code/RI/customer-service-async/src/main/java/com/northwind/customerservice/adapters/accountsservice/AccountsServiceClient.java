package com.northwind.customerservice.adapters.accountsservice;

import io.reactivex.Observable;

import java.util.Date;

public interface AccountsServiceClient {
    InvoiceModel[] getByCustomerId(long customerId, Date startDate, Date endDate);
    Observable<InvoiceModel> getByCustomerIdAsync(long customerId, Date startDate, Date endDate);
}
