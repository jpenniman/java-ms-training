package com.northwind.orderservice.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class When_creating_Orders {

    @Test
    public void customer_is_required() {

        long customerId = 1;
        String customerNo = "A0000000001";
        String companyName = "Acme Foods";

        Order order = new Order(customerId, customerNo, companyName);

        Assertions.assertEquals(customerId, order.getCustomerId());
        Assertions.assertEquals(customerNo, order.getCustomerNo());
        Assertions.assertEquals(companyName, order.getCompanyName());
    }

}
