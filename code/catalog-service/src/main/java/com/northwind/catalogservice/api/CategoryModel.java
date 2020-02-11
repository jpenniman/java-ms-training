package com.northwind.catalogservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class CategoryModel {
    @JsonProperty
    private long id;
    @JsonProperty
    private String categoryName;
    @JsonProperty
    private String desription;
    @JsonProperty
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
