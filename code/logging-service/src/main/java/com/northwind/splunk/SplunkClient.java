package com.northwind.splunk;

import java.util.List;

public interface SplunkClient {

    SplunkResponse send(SplunkRequest request);
    SplunkResponse send(List<SplunkRequest> request);

}
