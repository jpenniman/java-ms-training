package com.northwind.customerservice.proxies.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.northwind.customerservice.infrastructure.Loggable;
import com.northwind.customerservice.proxies.OrderClientConfig;
import com.northwind.customerservice.proxies.OrderModel;
import com.northwind.customerservice.proxies.OrderServiceClient;
import com.northwind.customerservice.infrastructure.RestTemplateFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class OrderServiceClientImpl implements OrderServiceClient {

    private RestTemplate restTemplate;
    private OrderClientConfig config;

    public OrderServiceClientImpl(OrderClientConfig config, RestTemplateFactory restTemplateFactory) {
        this.config = config;
        this.restTemplate = restTemplateFactory.getInstance("order-service");
    }

    @Override
    @Loggable
    @HystrixCommand(groupKey = "order-service", commandKey = "getOrderHistory")
    @Retryable(backoff = @Backoff(delay = 100, maxDelay = 1000, random = true))
    public OrderModel[] getOrderHistory(long customerId, Date startDate, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(String.format("%s/orders/history", config.getUrl()))
                .queryParam("customerId", Long.valueOf(customerId))
                .queryParam("startDate", dateFormat.format(startDate))
                .queryParam("endDate", dateFormat.format(endDate));

        String url = uri.toUriString();
        ResponseEntity<OrderModel[]> response = restTemplate.getForEntity(url, OrderModel[].class);

        return response.getBody();
    }
}
