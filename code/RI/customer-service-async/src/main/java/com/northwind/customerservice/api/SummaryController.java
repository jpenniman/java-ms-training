package com.northwind.customerservice.api;

import com.northwind.customerservice.domain.AccountSummary;
import com.northwind.customerservice.services.SummaryService;
import io.reactivex.Single;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/customers/{customerId}/summary")
public class SummaryController {
    private SummaryService service;

    public SummaryController(SummaryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Single<AccountSummary>> get(@PathVariable long customerId,
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Optional<Date> startDate,
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Optional<Date> endDate) {
        return ResponseEntity.ok(service.getSummery(customerId, startDate.get(), endDate.get()));
    }
}
