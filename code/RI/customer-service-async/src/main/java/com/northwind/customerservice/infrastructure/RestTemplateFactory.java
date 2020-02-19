package com.northwind.customerservice.infrastructure;

import org.springframework.web.client.RestTemplate;

public interface RestTemplateFactory {
    RestTemplate getInstance(String name);
}
