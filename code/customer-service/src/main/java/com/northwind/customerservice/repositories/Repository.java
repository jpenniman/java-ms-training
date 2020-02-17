package com.northwind.customerservice.repositories;

import io.reactivex.Observable;

import java.util.List;

public interface Repository<T> {
    T getById(long id);
    Observable<T> getAll(int offSet, int limit);
    T save(T entity);
    void delete(long id);
}
