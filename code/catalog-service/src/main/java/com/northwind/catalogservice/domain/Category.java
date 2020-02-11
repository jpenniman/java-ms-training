package com.northwind.catalogservice.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CategoryID")
    private long id;
    @Column
    private String categoryName;
    @Column
    private String description;
    @Version
    private long version;
    @Column
    private UUID objectId;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products;

    protected Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
        this.objectId = UUID.randomUUID();
        this.products = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVersion() {
        return version;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return objectId.equals(category.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
