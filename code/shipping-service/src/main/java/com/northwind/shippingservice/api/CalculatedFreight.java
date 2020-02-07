package com.northwind.shippingservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class CalculatedFreight {
    @JsonProperty
    private double freght;

    public double getFreght() {
        return freght;
    }

    public void setFreght(double freght) {
        this.freght = freght;
    }
}
