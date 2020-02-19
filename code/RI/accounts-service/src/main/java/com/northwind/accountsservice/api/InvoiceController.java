package com.northwind.accountsservice.api;

import com.northwind.accountsservice.services.InvoiceService;
import io.reactivex.Observable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }


    @GetMapping(params = {"customerId"})
    public ResponseEntity<Observable<InvoiceModel>> getByCustomerId(
            @RequestParam long customerId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Optional<Date> startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Optional<Date> endDate) {

        Observable<InvoiceModel> model =
                service.getByCustomerId(customerId, startDate, endDate)
                .map(invoice -> InvoiceMapper.toModel(invoice));
        return ResponseEntity.ok(model);
    }
}
