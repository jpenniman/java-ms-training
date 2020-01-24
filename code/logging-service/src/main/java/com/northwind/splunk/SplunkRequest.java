package com.northwind.splunk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class SplunkRequest {

    @JsonProperty
    private Object event;

    // Event is required. It's best practice to require required fields be
    // passed into the constructor.
    public SplunkRequest(Object event) {
        setEvent(event);
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        // Event is required, so we'll make sure it is never null.
        if (event == null)
            throw new IllegalArgumentException("Event is required.");

        this.event = event;
    }
}
