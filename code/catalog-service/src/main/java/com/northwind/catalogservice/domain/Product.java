package com.northwind.catalogservice.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ProductID")
    private long id;
    @Column
    private String productName;
    @Column
    private String quantityPerUnit;
    @Column
    private BigDecimal listPrice;
    @Column(name="Published")
    private boolean isPublished;
    @Version
    private long version;
    @Column
    private UUID objectId;

    @ManyToOne
    @JoinColumn(name="CategoryID")
    private Category category;

    protected Product () {}

    public Product(String productName) {
        this.productName = productName;
        this.objectId = UUID.randomUUID();
    }

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(String quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public long getVersion() {
        return version;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return objectId.equals(product.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
