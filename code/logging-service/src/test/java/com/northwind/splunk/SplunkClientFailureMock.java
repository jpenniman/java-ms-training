package com.northwind.splunk;

import java.util.List;

public class SplunkClientFailureMock implements SplunkClient {

    @Override
    public SplunkResponse send(SplunkRequest request) {
        return new SplunkResponse("Failure", 1);
    }

    @Override
    public SplunkResponse send(List<SplunkRequest> request) {
        return null;
    }
}
