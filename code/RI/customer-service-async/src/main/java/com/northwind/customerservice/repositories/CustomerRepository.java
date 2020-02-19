package com.northwind.customerservice.repositories;

import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;

import java.util.List;

public interface CustomerRepository extends Repository<Customer> {
    List<Customer> findByCompanyName(String companyName);
    Customer getByCustomerNo(String customerNo);

    Address addAddress(long customerId, Address address);
}
