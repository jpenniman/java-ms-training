package com.northwind.orderservice.infrastructure;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

public class RestTemplateFactory {

    private RestTemplateFactory() {}

    public final static RestTemplateFactory INSTANCE = new RestTemplateFactory();

    private final ConcurrentHashMap<String, RestTemplate> instances =
            new ConcurrentHashMap<String, RestTemplate>();

    public RestTemplate getInstance(String name, int connectionTimeout, int requestTimeout) {

        if (!instances.containsKey(name)) {
            synchronized (instances) {
                if (!instances.containsKey(name)) {
                    // HACK: NEVER NEVER NEVER NEVER NEVER DO THIS IN PRODUCTION
                    RestTemplate restTemplate = getRestTemplate(connectionTimeout, requestTimeout);
                    instances.put(name, restTemplate);
                }

            }
        }

        return instances.get(name);
    }

    public RestTemplate getRestTemplate(int connectionRequestTimeoutInMs, int requestTimeout) {

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectionRequestTimeoutInMs);
        requestFactory.setReadTimeout(requestTimeout);

        return new RestTemplate(requestFactory);
    }
}
