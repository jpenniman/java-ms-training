package com.northwind.customerservice.repositories;

import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;

import java.util.List;

public interface CustomerRepository extends Repository<Customer> {
    List<Customer> findByCompanyName(String companyName) throws RepositoryException;
    Customer getByCustomerNo(String customerNo) throws RepositoryException;

    Address addAddress(long customerId, Address address) throws RepositoryException;
}
