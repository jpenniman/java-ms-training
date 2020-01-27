package com.acme.customerservice.repositories;

import com.acme.customerservice.domain.Customer;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
class InMemoryCustomerRepository implements CustomerRepository{

    private static final HashMap<Long, Customer> data = new HashMap<Long, Customer>();

    private static volatile Long nextId = 1L;

    @Override
    public RepositoryResponse<Customer> save(Customer customer) {
        if (customer.getId() == 0) {
           return insert(customer);
        } else {
           return update(customer);
        }
    }

    @Override
    public Customer get(long id) {
        return data.get(id);
    }

    @Override
    public List<Customer> get() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(long id) {
        data.remove(id);
    }

    private RepositoryResponse<Customer> insert(Customer customer) {
        try {
            Field idField = Customer.class.getDeclaredField("id");
            idField.setAccessible(true);
            synchronized (nextId) {
                idField.setLong(customer, nextId++);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        data.put(customer.getId(), customer);
        return new RepositoryResponse<>(RepositoryResultStatus.SUCCESS, customer);
    }

    private RepositoryResponse<Customer> update(Customer customer) {
        Customer persistedCustomer = this.get(customer.getId());
        if (persistedCustomer.getVersion() != customer.getVersion())
        {
            return new RepositoryResponse<>(RepositoryResultStatus.CONFLICT, persistedCustomer);
        }

        customer.setVersion(persistedCustomer.getVersion() + 1);
        data.put(customer.getId(), customer);

        return new RepositoryResponse<>(RepositoryResultStatus.SUCCESS, customer);
    }
}
