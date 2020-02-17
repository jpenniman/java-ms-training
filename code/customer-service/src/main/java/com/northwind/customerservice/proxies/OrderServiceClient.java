package com.northwind.customerservice.proxies;

import com.northwind.customerservice.infrastructure.Loggable;

import java.util.Date;
import java.util.List;

public interface OrderServiceClient {

    @Loggable(message = "An error occurred while trying to get the order history")
    OrderModel[] getOrderHistory(long customerId, Date startDate, Date endDate);
}
