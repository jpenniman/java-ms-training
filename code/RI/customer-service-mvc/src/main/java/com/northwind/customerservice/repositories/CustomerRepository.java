package com.acme.customerservice.repositories;

import com.acme.customerservice.domain.Customer;

import java.util.List;

public interface CustomerRepository {
    RepositoryResponse<Customer> save(Customer customer);
    Customer get(long id);
    List<Customer> get();
    void delete(long id);
}
