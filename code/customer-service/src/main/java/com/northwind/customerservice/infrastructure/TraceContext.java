package com.northwind.customerservice.infrastructure;

import java.util.UUID;

public class TraceContext {
    private UUID traceId;

    public TraceContext() {
        traceId = UUID.randomUUID();
    }

    public UUID getTraceId() {
        return traceId;
    }

    public void setTraceId(UUID traceId) {
        this.traceId = traceId;
    }
}
