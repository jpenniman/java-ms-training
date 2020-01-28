package com.northwind.splunk;

import java.util.List;

public class SplunkClientMock implements SplunkClient {
    @Override
    public SplunkResponse send(SplunkRequest request) {
        return new SplunkResponse("Success", 0);
    }

    @Override
    public SplunkResponse send(List<SplunkRequest> request) {
        return null;
    }
}
