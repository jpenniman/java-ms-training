package com.northwind.shippingservice.api;

import com.northwind.shippingservice.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping/rates")
public class RateController {
    private RateService service;

    @Autowired
    public RateController(RateService service) {
        this.service = service;
    }

    @GetMapping(params = "country")
    public ResponseEntity<CalculatedFreight> get(@RequestParam String country) {
        CalculatedFreight freight = new CalculatedFreight();
        freight.setFreght(service.calcuateRate(country));
        return ResponseEntity.ok(freight);
    }

}
