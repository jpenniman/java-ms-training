package com.northwind.catalogservice.api;

import com.northwind.catalogservice.domain.Category;
import com.northwind.catalogservice.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // Asynchronous Controllers, taking advantage of the AsyncServlet API, must return
    // either Callable<T> or DeferredResult<T>
    @GetMapping
    public Callable<ResponseEntity<List<CategoryModel>>>  get() {
        Callable<ResponseEntity<List<CategoryModel>>> callable = ()->{
            return ResponseEntity.ok(service.getAll().stream()
                    .map(c->CategoryMapper.toModel(c))
                    .collect(Collectors.toList()));
        };
        return callable;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> get(@PathVariable long id) {
        Optional<Category> category = service.get(id);
        if (!category.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(CategoryMapper.toModel(category.get()));
    }

    @PostMapping
    public ResponseEntity<CategoryModel> create(@RequestBody CategoryModel model) {
        Category category = CategoryMapper.newCategory(model);
        category = service.save(category);

        return ResponseEntity.created(URI.create(String.format("/categories/%s", category.getId())))
                .body(CategoryMapper.toModel(category));
    }
}
