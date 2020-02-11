package com.northwind.catalogservice.api;

import com.northwind.catalogservice.domain.Product;
import com.northwind.catalogservice.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Multiple paths can be mapped to a single controller, so, for example,
// if we want to allow a product to not be associated with a category,
// we an map both /products and /categories/{categoryId}/products here.
@RestController
@RequestMapping({"/products","/categories/{categoryId}/products"})
public class ProductController {
    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> get(@PathVariable Optional<Long> categoryId) {
        if (categoryId.isPresent()) {
            return ResponseEntity.ok(service.getAllByCategory(categoryId.get()).stream()
                        .map(p->ProductMapper.toModel(p))
                        .collect(Collectors.toList()));
        } else {
            return ResponseEntity.ok(service.getAll().stream()
                    .map(c -> ProductMapper.toModel(c))
                    .collect(Collectors.toList()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> get(@PathVariable long id) {
        Optional<Product> Product = service.get(id);
        if (!Product.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(ProductMapper.toModel(Product.get()));
    }

    @PostMapping
    public ResponseEntity<ProductModel> create(@PathVariable Optional<Long> categoryId, @RequestBody ProductModel model) {
        Product product = ProductMapper.newProduct(model);

        if (!categoryId.isPresent()) {
            if (model.getCategory() != null && model.getCategory().getId() > 0)
            categoryId = Optional.of(model.getCategory().getId());
        }

        product = service.save(product, categoryId);

        return ResponseEntity.created(URI.create(String.format("/categories/%s", product.getId())))
                .body(ProductMapper.toModel(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> update(@PathVariable Optional<Long> categoryId, @PathVariable long id, @RequestBody ProductModel model) {
        Product product = ProductMapper.newProduct(model);

        if (!categoryId.isPresent()) {
            if (model.getCategory() != null && model.getCategory().getId() > 0)
                categoryId = Optional.of(model.getCategory().getId());
        }

        product = service.save(product, categoryId);

        return ResponseEntity.ok(ProductMapper.toModel(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Optional<Long> categoryId, @PathVariable long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
