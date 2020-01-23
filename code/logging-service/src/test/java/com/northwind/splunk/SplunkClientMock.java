package com.northwind.splunk;

public class SplunkClientMock implements SplunkClient {
    @Override
    public SplunkResponse send(SplunkRequest request) {
        return new SplunkResponse("Success", 0);
    }
}
