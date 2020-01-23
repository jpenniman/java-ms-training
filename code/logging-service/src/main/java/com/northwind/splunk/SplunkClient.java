package com.northwind.splunk;

public interface SplunkClient {

    SplunkResponse send(SplunkRequest request);
}
