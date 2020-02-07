package com.northwind.shippingservice.services;

import com.northwind.shippingservice.repositories.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {
    private RatesRepository repository;

    @Autowired
    public RateService(RatesRepository repository) {
        this.repository = repository;
    }

    public double calcuateRate(String country) {
        return repository.findByCountry(country).getFlatRate();
    }
}
