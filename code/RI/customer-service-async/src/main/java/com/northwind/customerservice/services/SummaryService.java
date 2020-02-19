package com.northwind.customerservice.services;

import com.northwind.customerservice.adapters.accountsservice.AccountsServiceClient;
import com.northwind.customerservice.adapters.accountsservice.InvoiceModel;
import com.northwind.customerservice.adapters.orderservice.OrderModel;
import com.northwind.customerservice.adapters.orderservice.OrderServiceClient;
import com.northwind.customerservice.domain.AccountSummary;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class SummaryService {
    private AccountsServiceClient accountsServiceClient;
    private OrderServiceClient orderServiceClient;
    private CustomerService customerService;

    public SummaryService(AccountsServiceClient accountsServiceClient, OrderServiceClient orderServiceClient, CustomerService customerService) {
        this.accountsServiceClient = accountsServiceClient;
        this.orderServiceClient = orderServiceClient;
        this.customerService = customerService;
    }

    public Single<AccountSummary> getSummery(long customerId, Date startDate, Date endDate) {
        Observable<AccountSummary> sum = customerService.getById(customerId)
                .toObservable()
                .flatMap(customer->{
                        Observable<BigDecimal> invoicesTotal = accountsServiceClient.getByCustomerIdAsync(customerId, startDate, endDate)
                                .map(invoice->invoice.getTotal())
                                .reduce(BigDecimal.ZERO, (t1, t2)->t1.add(t2))
                                .toObservable();
                        Observable<Long> ordersCount = orderServiceClient.getOrderHistoryAsync(customerId, startDate, endDate)
                                .count()
                                .toObservable();


                    return Observable.zip(invoicesTotal, ordersCount, (i,o)->{
                        AccountSummary summary = new AccountSummary();
                        summary.setCustomer(customer.get());
                        summary.setNumberOfOrders(o.intValue());
                        summary.setTotalInvoices(i);
                        return summary;
                    });
                 });
        return sum.single(new AccountSummary());
    }
}
