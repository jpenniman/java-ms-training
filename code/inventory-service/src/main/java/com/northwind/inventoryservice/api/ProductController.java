package com.northwind.inventoryservice.api;

import com.northwind.inventoryservice.domain.Product;
import com.northwind.inventoryservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> get(@RequestParam Optional<Integer> offset, @RequestParam Optional<Integer> limit) {
        return ResponseEntity.ok(service.getAll(offset, limit).stream()
                .map(p->ProductMapper.toModel(p))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> get(@PathVariable long id) {
        Optional<Product> product = service.get(id);
        if (!product.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProductMapper.toModel(product.get()));
    }

    @PostMapping
    public ResponseEntity<ProductModel> create(@RequestBody ProductModel model) {
        Product product = ProductMapper.newEntity(model);
        product = service.save(product);
        return ResponseEntity.created(URI.create(String.format("/products/%s", product.getId())))
                .body(ProductMapper.toModel(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> update(@PathVariable long id, @RequestBody ProductModel model) {
        Optional<Product> product = service.get(id);
        if (!product.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (product.get().getVersion() > model.getVersion()) {
            return new ResponseEntity<>(ProductMapper.toModel(product.get()), HttpStatus.CONFLICT);
        }
        Product savedProduct = service.save(product.get());

        return ResponseEntity.ok(ProductMapper.toModel(savedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
