package com.northwind.customerservice.adapters.accountsservice.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.northwind.customerservice.adapters.accountsservice.AccountsServiceClient;
import com.northwind.customerservice.adapters.accountsservice.AccountsServiceClientConfig;
import com.northwind.customerservice.adapters.accountsservice.InvoiceModel;
import com.northwind.customerservice.infrastructure.RestTemplateFactory;
import io.reactivex.Observable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
class AccountsServiceImpl implements AccountsServiceClient {
    private RestTemplate restTemplate;
    private AccountsServiceClientConfig config;

    AccountsServiceImpl(AccountsServiceClientConfig config, RestTemplateFactory restTemplateFactory) {
        this.config = config;
        this.restTemplate = restTemplateFactory.getInstance("account-service");
    }

    @Override
    @HystrixCommand(groupKey = "account-service", commandKey = "getByCustomerId", fallbackMethod = "getByCustomerIdFallback")
    @Retryable
    public InvoiceModel[] getByCustomerId(long customerId, Date startDate, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(config.getUrl())
                .path("/invoices")
                .queryParam("customerId", customerId)
                .queryParam("startDate", dateFormat.format(startDate))
                .queryParam("endDate", dateFormat.format(endDate));
        return restTemplate.getForObject(url.build().toString(), InvoiceModel[].class);
    }

    @Override
    public Observable<InvoiceModel> getByCustomerIdAsync(long customerId, Date startDate, Date endDate) {
        return Observable.fromArray(getByCustomerId(customerId, startDate, endDate));
    }

    private InvoiceModel[] getByCustomerIdFallback(long customerId, Date startDate, Date endDate) {
        return new InvoiceModel[0];
    }
}
