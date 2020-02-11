package com.northwind.orderservice.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private long id;

    @Column(name = "CustomerID")
    private long customerId;
    @Column(name = "CustomerNo", length = 11)
    private String customerNo;
    @Column(name = "CustomerName", length = 50)
    private String companyName;
    @Column(name = "OrderDate")
    private Date orderDate;
    @Column(name = "RequiredDate")
    private Date requiredDate;
    @Column(name = "ShippedDate")
    private Date shippedDate;
    @Column(name = "Freight")
    private BigDecimal freight;

    @Column(name = "ShipName", length = 40)
    private String shipName;
    @Column(name = "ShipAddress", length = 60)
    private String shipAddress;
    @Column(name = "ShipCity", length = 15)
    private String shipCity;
    @Column(name = "ShipRegion", length = 15)
    private String shipRegion;
    @Column(name = "ShipPostalCode", length = 10)
    private String shipPostalCode;
    @Column(name = "ShipCountry", length = 15)
    private String shipCountry;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true,
    fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    @Version
    private long version;

    @Enumerated(EnumType.STRING)
    @Column(name="Status")
    private OrderStatus status;

    @Column(name = "ObjectID", length = 36)
    private UUID objectId;

    protected Order() {}

    public Order(long customerId, String customerNo, String companyName) {
        this.customerId = customerId;
        this.customerNo = customerNo;
        this.companyName = companyName;
        this.status = OrderStatus.Processing;
        this.objectId = UUID.randomUUID();
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipRegion() {
        return shipRegion;
    }

    public void setShipRegion(String shipRegion) {
        this.shipRegion = shipRegion;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

    public long getVersion() {
        return version;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void orderShipped(java.util.Date shippedDate, long shippedVia) {
        if (status == OrderStatus.Cancelled) {
            throw new IllegalStateException("Cannot ship order. Order has been cancelled.");
        }
        this.shippedDate = new Date(shippedDate.getTime());
        status = OrderStatus.Shipped;
    }

    public void cancelOrder() {
        if (status == OrderStatus.Shipped) {
            throw new IllegalStateException("Cannot cancel order. Order has been shipped.");
        }
        status = OrderStatus.Cancelled;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(OrderItem item) {
        item.getId().setOrderId(this.id);
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public BigDecimal getOrderTotal() {
        BigDecimal total = items.stream()
                .map(i->i.getItemTotal())
                .reduce((t1,t2)->t1.add(t2))
                .orElse(new BigDecimal(0));

        return total.add(freight);
    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Order))
            return false;

        Order other = (Order)obj;
        return this.objectId.equals(other.objectId);
    }
}
