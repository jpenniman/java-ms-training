package com.northwind.customerservice.repositories;

import java.util.List;

public interface Repository<T> {
    T getById(long id);
    List<T> getAll(int offSet, int limit);
    T save(T entity);
    void delete(long id);
}
