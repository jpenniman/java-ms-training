package com.northwind.orderservice.adapters;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class ShippingServiceClientImpl implements ShippingServiceClient {

    private RestTemplate restTemplate;
    private ShippingServiceClientConfig config;

    public ShippingServiceClientImpl(ShippingServiceClientConfig config, RestTemplateBuilder restTemplateBuilder) {
        this.config = config;
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Override
    @Retryable(maxAttempts = 3,
               backoff = @Backoff(delay = 100, maxDelay = 2000, random = true))
    @HystrixCommand(groupKey = "shipping-service",
                    commandKey = "getFreightAmount",
                    fallbackMethod = "getFreightAmountFallback")
    public double getFreightAmount(String country) {
        ResponseEntity<ShippingRateModel> model = restTemplate.getForEntity(
                config.getUrl() + "/shipping/rates?country="+country,
                    ShippingRateModel.class);

        return model.getBody().getFreight();
    }

    private double getFreightAmountFallback(String country) {
        throw new RuntimeException("Cannot calculate freight at this time.");
    }
}
