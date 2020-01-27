package com.northwind.splunk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.framework.RestTemplateFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SplunkClientImpl implements SplunkClient {

    private RestTemplateFactory restTemplateFactory =
            RestTemplateFactory.INSTANCE;

    private HttpHeaders headers;

    private ObjectMapper objectMapper;

    public SplunkClientImpl() {
        objectMapper = new ObjectMapper();
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

    @Override
    public SplunkResponse send(List<SplunkRequest> request) {
        RestTemplate client = restTemplateFactory.getInstance("splunk");

        StringBuilder sb = new StringBuilder();

        for(SplunkRequest req : request) {
            try {
                String json = objectMapper.writeValueAsString(req);
                sb.append(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(sb.toString(), headers);

        String url = "https://localhost:8088/services/collector/event";
        return client.postForObject(url, httpEntity, SplunkResponse.class);
    }
}
