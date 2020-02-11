package com.northwind.inventoryservice.services;

import com.northwind.inventoryservice.domain.Product;
import com.northwind.inventoryservice.repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll(Optional<Integer> offset, Optional<Integer> limit) {
        int pageSize = limit.orElse(10);
        int page = (offset.orElse(0) / pageSize) + 1;
        return repository.findAll(PageRequest.of(page, pageSize)).toList();
    }

    public Optional<Product> get(long id) {
        return repository.findById(id);
    }

    public Product save(Product product) {
        return repository.saveAndFlush(product);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
