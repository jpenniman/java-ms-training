package com.northwind.shippingservice.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="ShippingRates")
public class Rate {
    @Id
    @GeneratedValue
    @Column(name="ShippingRateID")
    private long id;
    @Column(name="Country", length=15)
    private String country;
    @Column(name="FlatRate")
    private double flatRate;
    @Version
    private long version;
    @Column(name="ObjectID", length=36)
    private UUID objectID;

    public long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(double flatRate) {
        this.flatRate = flatRate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }
}
