package com.northwind.customerservice.infrastructure;

import java.util.UUID;

public class TraceContext {

    public final static String TRACE_ID_HEADER="X-B3-TraceId";

    private String traceId;

    public TraceContext() {
        traceId = UUID.randomUUID().toString();
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
