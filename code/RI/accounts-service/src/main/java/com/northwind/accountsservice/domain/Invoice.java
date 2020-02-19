package com.northwind.accountsservice.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name="Invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InvoiceID")
    private long id;
    @Column
    private Date invoiceDate;
    @Column
    private long orderNo;
    @Column
    private long customerId;
    @Column
    private String customerNo;
    @Column
    private String customerName;
    @Column
    private BigDecimal freight;
    @Column
    private boolean isPaid;
    @Version
    private long version;
    @Column(name="ObjectID")
    private UUID objectId;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceDetail> details;

    public Invoice() {
        details = new ArrayList<>();
        invoiceDate = Calendar.getInstance().getTime();
        version = 1;
        objectId = UUID.randomUUID();
    }

    public long getId() {
        return id;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public long getVersion() {
        return version;
    }

    public List<InvoiceDetail> getDetails() {
        return Collections.unmodifiableList(details);
    }

    public BigDecimal getSubtotal() {
        return details.stream()
                .map(d->d.getAmount().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce((d1, d2)->d1.add(d2))
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getFreight());
    }

    public InvoiceDetail addDetail(long productId, String productName, int quantity, BigDecimal amount) {
        InvoiceDetail detail = new InvoiceDetail(this);
        detail.setProductId(productId);
        detail.setProductName(productName);
        detail.setQuantity(quantity);
        detail.setAmount(amount);
        details.add(detail);
        return detail;
    }

    public void removeDetail(long detailId) {
        Optional<InvoiceDetail> detail = details.stream().filter(d->d.getId() == detailId).findFirst();
        if (detail.isPresent())
            details.remove(detail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return objectId.equals(invoice.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
