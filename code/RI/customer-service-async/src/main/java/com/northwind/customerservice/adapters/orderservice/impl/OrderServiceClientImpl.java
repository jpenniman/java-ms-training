package com.northwind.customerservice.adapters.orderservice.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.northwind.customerservice.infrastructure.Loggable;
import com.northwind.customerservice.adapters.orderservice.OrderClientConfig;
import com.northwind.customerservice.adapters.orderservice.OrderModel;
import com.northwind.customerservice.adapters.orderservice.OrderServiceClient;
import com.northwind.customerservice.infrastructure.RestTemplateFactory;
import io.reactivex.Observable;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;

public class OrderServiceClientImpl implements OrderServiceClient {

    private RestTemplate restTemplate;
    private OrderClientConfig config;

    public OrderServiceClientImpl(OrderClientConfig config, RestTemplateFactory restTemplateFactory) {
        this.config = config;
        this.restTemplate = restTemplateFactory.getInstance("order-service");
    }

    @Override
    //@Loggable
   // @Retryable(backoff = @Backoff(delay = 100, maxDelay = 1000, random = true))
    @HystrixCommand(groupKey = "order-service", commandKey = "getOrderHistory", fallbackMethod = "getOrderHistoryFallback")
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

    private OrderModel[] getOrderHistoryFallback(long customerId, Date startDate, Date endDate){
        return new OrderModel[0];
    }

    @Override
    public Observable<OrderModel> getOrderHistoryAsync(long customerId, Date startDate, Date endDate) {
        return Observable.fromArray(getOrderHistory(customerId, startDate, endDate));
    }


}
