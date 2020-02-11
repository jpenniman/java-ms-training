package com.northwind.orderservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "OrderDetails")
public class OrderItem {

    @EmbeddedId
    private OrderItemKey id;

    @Column(name="ProductName", length = 40)
    private String productName;
    @Column(name="QuantityPerUnit", length = 20)
    private String quantityPerUnit;
    @Column(name="UnitPrice")
    private BigDecimal unitPrice;
    @Column(name="Quantity")
    private int quantity;
    @Column(name="Discount")
    private float discount;
    @Version
    private long version;

    @JsonIgnore
    @MapsId("orderId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "OrderID")
    private Order order;

    @Column(name="ObjectID")
    private UUID objectId;

    protected OrderItem() {}

    public OrderItem(Order order, long productId) {
        this.id = new OrderItemKey();
        this.order = order;
        this.id.setOrderId(order.getId());
        this.id.setProductId(productId);
    }

    public OrderItemKey getId() {
        return id;
    }

    public void setId(OrderItemKey id) {
        this.id = id;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public BigDecimal getItemTotal() {
        BigDecimal undiscountedTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal total = undiscountedTotal;
        if (discount > 0)
            total = undiscountedTotal.multiply(BigDecimal.valueOf(discount));

        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return objectId.equals(orderItem.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
