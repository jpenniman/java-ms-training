package com.northwind.orderservice.adapters;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.northwind.orderservice.infrastructure.RestTemplateFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShippingServiceClientImpl implements ShippingServiceClient {

    private RestTemplate restTemplate = RestTemplateFactory.INSTANCE.getInstance("shipping", 30000, 30000);

    @Override
    @Retryable(maxAttempts = 3,
               backoff = @Backoff(delay = 100, maxDelay = 2000, random = true))
    @HystrixCommand(groupKey = "shipping-service",
                    commandKey = "getFreightAmount",
                    fallbackMethod = "getFreightAmountFallback")
    public double getFreightAmount(String country) {
        ResponseEntity<ShippingRateModel> model = restTemplate.getForEntity(
                "http://localhost:8085/shipping/rates?country="+country,
                    ShippingRateModel.class);

        return model.getBody().getFlatRate();
    }

    private double getFreightAmountFallback(String country) {
        throw new RuntimeException("Cannot calculate freight at this time.");
    }
}
