package com.northwind.accountsservice.repositories;

import com.northwind.accountsservice.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerIdAndInvoiceDateBetween(long customerId, Date startDate, Date endDate);
}
