package com.northwind.customerservice.repositories;

import java.util.List;

public interface Repository<T> {
    T getById(long id) throws RepositoryException;
    List<T> getAll(int offSet, int limit) throws RepositoryException;
    T save(T entity) throws RepositoryException;
    void delete(long id) throws RepositoryException;
}
