package com.northwind.customerservice.services;

import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.repositories.CustomerRepository;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;
import java.util.Optional;

public class CustomerService {

    private CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Single<Optional<Customer>> getById(long id) {
        return repository.getById(id);
    }

    public Observable<Customer> getAll(int offset, int limit) {
        return repository.getAll(offset, limit);
    }

    public List<Customer> findByCompanyName(String companyName) {
        return repository.findByCompanyName(companyName);
    }

    public Customer getByCustomerNo(String customerNo) {
        return repository.getByCustomerNo(customerNo);
    }

    public void delete(Customer customer) {
        repository.delete(customer.getId());
    }

    public Customer save(Customer customer) {
        Customer savedCustomer = repository.save(customer);
        if (savedCustomer.getCustomerNo() == null) {
            savedCustomer.generateCustomerNo();
            return repository.save(savedCustomer);
        }
        return savedCustomer;
    }

    public Address addAddress(long customerId, Address address) {
        return repository.addAddress(customerId, address);
    }
}
