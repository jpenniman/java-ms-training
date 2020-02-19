package com.northwind.customerservice.adapters.orderservice;

import com.northwind.customerservice.infrastructure.Loggable;
import io.reactivex.Observable;

import java.util.Date;

public interface OrderServiceClient {

    @Loggable(message = "An error occurred while trying to get the order history")
    OrderModel[] getOrderHistory(long customerId, Date startDate, Date endDate);
    Observable<OrderModel> getOrderHistoryAsync(long customerId, Date startDate, Date endDate);
}
