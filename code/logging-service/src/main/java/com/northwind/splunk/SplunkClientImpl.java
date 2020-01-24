package com.northwind.splunk;

import com.northwind.framework.RestTemplateFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class SplunkClientImpl implements SplunkClient {

    private RestTemplateFactory restTemplateFactory =
            RestTemplateFactory.INSTANCE;

    private HttpHeaders headers;

    public SplunkClientImpl() {
        headers = new HttpHeaders();
        headers.add("Authorization", "Splunk abcd1234");
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public SplunkResponse send(SplunkRequest request) {
        RestTemplate client = restTemplateFactory.getInstance("splunk");

        HttpEntity<SplunkRequest> httpEntity = new HttpEntity<>(request, headers);

        String url = "https://localhost:8088/services/collector/event";
        return client.postForObject(url, httpEntity, SplunkResponse.class);
    }
}
