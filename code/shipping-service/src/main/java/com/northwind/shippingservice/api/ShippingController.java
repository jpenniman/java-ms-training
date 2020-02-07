package com.northwind.shippingservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northwind.shippingservice.services.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private ShippingService service;

    public ShippingController(ShippingService service) {
        this.service = service;
    }

    @PostMapping("/notify-shipped")
    public ResponseEntity notifyOrderShipped(@RequestBody NotifyOrderShippedRequest request) throws JsonProcessingException {
        service.notifyOrderShipped(request.getOrderNo());
        return ResponseEntity.accepted().build();
    }
}
