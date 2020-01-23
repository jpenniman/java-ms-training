package com.northwind.splunk;

public class SplunkClientFailureMock implements SplunkClient {

    @Override
    public SplunkResponse send(SplunkRequest request) {
        return new SplunkResponse("Failure", 1);
    }
}
