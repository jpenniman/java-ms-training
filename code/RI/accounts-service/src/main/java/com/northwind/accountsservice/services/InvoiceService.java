package com.northwind.accountsservice.services;

import com.northwind.accountsservice.domain.Invoice;
import com.northwind.accountsservice.repositories.InvoiceRepository;
import io.reactivex.Observable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class InvoiceService {
    private InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
    }


    public Observable<Invoice> getByCustomerId(long customerId, Optional<Date> startDate, Optional<Date> endDate) {

        return Observable.create(subsriber->{
            Date periodStart = startDate.orElse(Date.from(Instant.now().minus(Duration.ofDays(30))));
            Date periodEnd = endDate.orElse(Date.from(Instant.now()));

            if (periodStart.after(periodEnd)) {
                subsriber.onError(new IllegalArgumentException("startDate must be before endDate"));
            } else {
                repository.findByCustomerIdAndInvoiceDateBetween(customerId, periodStart, periodEnd)
                        .stream()
                        .forEach(invoice -> subsriber.onNext(invoice));
            }
            subsriber.onComplete();
        });
    }
}
