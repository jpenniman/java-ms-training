package com.northwind.customerservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
class AddressModel {
    @JsonProperty
    private long id; // {readonly}
    @JsonProperty
    private String streetOrPoBox;//:string {len > 0, len <= 60, !whitespace, required}
    @JsonProperty
    private String city;//:string { len <= 30, !whitespace }
    @JsonProperty
    private String stateOrProvince;//:string { len <= 15, !whitespace}
    @JsonProperty
    private String postalCode;//:string { len <= 10, !whitespace, required }
    @JsonProperty
    private String country;//:string { len <= 15, !whitespace }
    @JsonProperty
    private boolean isDefaultBilling;
    @JsonProperty
    private boolean isDefaultShipping;
    @JsonProperty
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreetOrPoBox() {
        return streetOrPoBox;
    }

    public void setStreetOrPoBox(String streetOrPoBox) {
        this.streetOrPoBox = streetOrPoBox;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isDefaultBilling() {
        return isDefaultBilling;
    }

    public void setDefaultBilling(boolean defaultBilling) {
        isDefaultBilling = defaultBilling;
    }

    public boolean isDefaultShipping() {
        return isDefaultShipping;
    }

    public void setDefaultShipping(boolean defaultShipping) {
        isDefaultShipping = defaultShipping;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
