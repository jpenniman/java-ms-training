package com.northwind.customerservice.api;

import com.northwind.customerservice.domain.PurchaseHistory;
import com.northwind.customerservice.adapters.orderservice.OrderModel;
import com.northwind.customerservice.services.OrderHistoryService;
import io.reactivex.Observable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/customers/{customerId}/history")
public class OrderHistoryController {

    private OrderHistoryService service;

    public OrderHistoryController(OrderHistoryService service) {
        this.service = service;
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderModel[]> getOrderHistory(@PathVariable long customerId,
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        @RequestParam Date startDate,
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        @RequestParam Date endDate) {

        return ResponseEntity.ok(service.getOrderHistory(customerId, startDate, endDate));
    }

    @GetMapping("/purchase-history")
    public ResponseEntity<Observable<PurchaseHistory>> getPurchaseHistory(@PathVariable long customerId,
                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                          @RequestParam Date month1,
                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                          @RequestParam Date month2) {
        return ResponseEntity.ok(service.getPurchaseHistoryAsync(customerId, month1, month2));
    }
}
