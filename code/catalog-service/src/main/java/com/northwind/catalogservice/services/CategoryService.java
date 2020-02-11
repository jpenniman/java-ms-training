package com.northwind.catalogservice.services;

import com.northwind.catalogservice.domain.Category;
import com.northwind.catalogservice.repository.CategoryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Optional<Category> get(long id){
        return repository.findById(id);
    }

    public Category save(Category entity) {
        return repository.saveAndFlush(entity);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
