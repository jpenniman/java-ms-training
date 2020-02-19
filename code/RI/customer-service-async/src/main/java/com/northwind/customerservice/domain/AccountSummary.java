package com.northwind.customerservice.domain;

import java.math.BigDecimal;

public class AccountSummary {
    private Customer customer;
    private int numberOfOrders;
    private BigDecimal totalInvoices;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public BigDecimal getTotalInvoices() {
        return totalInvoices;
    }

    public void setTotalInvoices(BigDecimal totalInvoices) {
        this.totalInvoices = totalInvoices;
    }
}
