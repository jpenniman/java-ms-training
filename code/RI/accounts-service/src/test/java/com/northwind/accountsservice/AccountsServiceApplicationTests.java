package com.northwind.accountsservice;

import com.northwind.accountsservice.domain.Invoice;
import com.northwind.accountsservice.repositories.InvoiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class AccountsServiceApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private InvoiceRepository repository;

	@Test
	void contextLoads() {
	}

	@Test
	void invoiceFetch() {
		List<Invoice> invoices = repository.findAll();
		Assertions.assertTrue(invoices.size() > 0);
	}

	@Test
	void invoiceGetByCustomerId() {
		Date startDate = Date.from(java.time.LocalDate.parse("1997-10-01").atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(java.time.LocalDate.parse("1997-10-31").atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<Invoice> invoices = repository.findByCustomerIdAndInvoiceDateBetween(1, startDate, endDate);
		Assertions.assertEquals(2, invoices.size());
	}

	//448
	@Test
	void fetchByIdWithDetails() {

		Invoice invoice = repository.findById(448L).get();

		Assertions.assertEquals(1, invoice.getDetails().size());
	}
}
