package com.northwind.customerservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.StringJoiner;

@JsonSerialize
public class ApiError {
    @JsonProperty
    private String detail;
    @JsonProperty
    private String source;
    @JsonProperty
    private String title;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("%s while calling %s Details: %s", title, source, detail);
    }
}
