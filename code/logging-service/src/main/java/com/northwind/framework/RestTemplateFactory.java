package com.northwind.framework;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

public class RestTemplateFactory {

    private RestTemplateFactory() {}

    public final static RestTemplateFactory INSTANCE = new RestTemplateFactory();

    private final ConcurrentHashMap<String, RestTemplate> instances =
            new ConcurrentHashMap<String, RestTemplate>();

    public RestTemplate getInstance(String name) {

        if (!instances.containsKey(name)) {
            synchronized (instances) {
                if (!instances.containsKey(name))
                    instances.put(name, new RestTemplate());
            }
        }

        return instances.get(name);
    }
}
