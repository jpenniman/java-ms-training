package com.northwind.catalogservice.services;

import com.northwind.catalogservice.domain.Category;
import com.northwind.catalogservice.domain.Product;
import com.northwind.catalogservice.repository.ProductRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private ProductRepository repository;
    private CategoryService categoryService;
    private MeterRegistry meterRegistry;

    public ProductService(ProductRepository repository, CategoryService categoryService, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.meterRegistry = meterRegistry;
    }

    public List<Product> getAll() {
        meterRegistry.counter("product.get-all").increment();
        Instant start = Instant.now();
        List<Product> results = repository.findAll();
        Instant stop = Instant.now();
        meterRegistry.timer("product-repository.find-all")
                .record(Duration.between(start, stop).toMillis(), TimeUnit.MILLISECONDS);
        return results;
    }

    public List<Product> getAllByCategory(long categoryId) {
        return categoryService.get(categoryId).get().getProducts();
    }

    public Optional<Product> get(long id){
        return repository.findById(id);
    }

    public Product save(Product entity, Optional<Long> categoryId) {
        if (categoryId.isPresent()) {
            Optional<Category> category = categoryService.get(categoryId.get());
            entity.setCategory(category.get());
        }
        return repository.saveAndFlush(entity);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
