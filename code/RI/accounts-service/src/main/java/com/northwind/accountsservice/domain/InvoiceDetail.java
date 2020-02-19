package com.northwind.accountsservice.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "InvoiceDetails")
public class InvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="InvoiceDetailsID")
    private long id;
    @Column
    private long productId;
    @Column
    private String productName;
    @Column
    private int quantity;
    @Column
    private BigDecimal amount;
    @Version
    private long version;
    @Column(name = "ObjectID")
    private UUID objectId;

    @ManyToOne
    @JoinColumn(name = "InvoiceID")
    private Invoice invoice;

    protected InvoiceDetail() {}

    InvoiceDetail(Invoice invoice) {
        this.invoice = invoice;
        version = 1;
        objectId = UUID.randomUUID();
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getVersion() {
        return version;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDetail that = (InvoiceDetail) o;
        return objectId.equals(that.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
