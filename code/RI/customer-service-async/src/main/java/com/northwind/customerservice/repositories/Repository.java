package com.northwind.customerservice.repositories;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    Single<Optional<T>> getById(long id);
    Observable<T> getAll(int offSet, int limit);
    T save(T entity);
    void delete(long id);
}
