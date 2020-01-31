package com.northwind.customerservice.repositories.impl;

import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.repositories.CustomerRepository;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryCustomerRepository implements CustomerRepository {
    private Map<Long, Customer> data = new HashMap<Long, Customer>();

    @Override
    public List<Customer> findByCompanyName(String companyName) {
        return data.values().stream()
                .filter(c->c.getCompanyName().startsWith(companyName))
                .collect(Collectors.toList());
    }

    @Override
    public Customer getByCustomerNo(String customerNo) {
        return data.values().stream()
                .filter(c->c.getCustomerNo().equalsIgnoreCase(customerNo))
                .findFirst()
                .get();
    }

    @Override
    public Address addAddress(long customerId, Address address) {
        return null;
    }

    @Override
    public Customer getById(long id) {
        return data.get(id);
    }

    @Override
    public List<Customer> getAll(int offSet, int limit) {
        return data.values().stream()
                .skip(offSet)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Customer save(Customer entity) {
        if (entity.getId() == 0) {
            long id = data.keySet().stream()
                    .max(Long::compare)
                    .orElse(1L);
            entity.setId(id);
        }
        entity.setVersion(entity.getVersion() + 1);
        data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        data.remove(id);
    }
}
