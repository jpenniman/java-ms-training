package com.northwind.customerservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

@JsonSerialize
public class PurchaseHistory {

    @JsonProperty
    private Date monthEnd;

    @JsonProperty
    private BigDecimal totalPurchases;

    public PurchaseHistory(Date monthEnd, BigDecimal totalPurchases) {
        this.monthEnd = monthEnd;
        this.totalPurchases = totalPurchases;
    }

    public Date getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(Date monthEnd) {
        this.monthEnd = monthEnd;
    }

    public BigDecimal getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(BigDecimal totalPurchases) {
        this.totalPurchases = totalPurchases;
    }
}
