package com.northwind.customerservice.domain;

public class Address {
    private long id; // {readonly}
    private String streetOrPoBox;//:string {len > 0, len <= 60, !whitespace, required}
    private String city;//:string { len <= 30, !whitespace }
    private String stateOrProvince;//:string { len <= 15, !whitespace}
    private String postalCode;//:string { len <= 10, !whitespace, required }
    private String country;//:string { len <= 15, !whitespace }
    private boolean isDefaultBilling;
    private boolean isDefaultShipping;
    private long version;

    public Address(String streetOrPoBox, String postalCode) {
        setStreetOrPoBox(streetOrPoBox);
        setPostalCode(postalCode);
    }

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
        if (streetOrPoBox == null)
            throw new IllegalArgumentException("StreetOrPoBox is required.");

        String cleanStreetOrPoBox = streetOrPoBox.trim();
        if (cleanStreetOrPoBox.length() == 0 || cleanStreetOrPoBox.length() > 60)
            throw new IllegalArgumentException("StreetOrPoBox must be between 1 and 60 characters.");

        this.streetOrPoBox = cleanStreetOrPoBox;
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
        if (postalCode == null)
            throw new IllegalArgumentException("PostalCode is required.");

        String cleanPostalCode = postalCode.trim();
        if (cleanPostalCode.length() == 0 || cleanPostalCode.length() > 60)
            throw new IllegalArgumentException("PostalCode must be between 1 and 60 characters.");

        this.postalCode = cleanPostalCode;
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
