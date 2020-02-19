package com.northwind.customerservice.services;

import com.northwind.customerservice.domain.PurchaseHistory;
import com.northwind.customerservice.infrastructure.LoggerFactory;
import com.northwind.customerservice.adapters.orderservice.OrderModel;
import com.northwind.customerservice.adapters.orderservice.OrderServiceClient;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

public class OrderHistoryService {

    private OrderServiceClient orderServiceClient;
    private Log logger;

    public OrderHistoryService(OrderServiceClient orderServiceClient, LoggerFactory loggerFactory) {
        this.orderServiceClient = orderServiceClient;
        this.logger = loggerFactory.getLog(OrderHistoryService.class);
    }

    public OrderModel[] getOrderHistory(long customerId, Date startDate, Date endDate) {
        return orderServiceClient.getOrderHistory(customerId, startDate, endDate);
    }

    private Callable<BigDecimal> getMonthlyTotal(long customerId, Date monthEnd) {
        return () -> {
            try {
                Calendar monthStart = Calendar.getInstance();
                monthStart.setTime(monthEnd);
                monthStart.set(Calendar.DAY_OF_MONTH, 1);
                OrderModel[] orders = getOrderHistory(customerId, monthStart.getTime(), monthEnd);
                return Arrays.stream(orders).map(o -> o.getTotal()).reduce((i1, i2) -> i1.add(i2)).orElse(BigDecimal.ZERO);
            } catch (Exception e) {
                logger.warn(String.format("Unable to get order history for month ending in: %s",monthEnd), e );
                return BigDecimal.ZERO;
            }
        };
    }



    public List<PurchaseHistory> getPurchaseHistory(long customerId, Date monthEnd1, Date monthEnd2) {

        Future<BigDecimal> month1 = Executors.newCachedThreadPool().submit(getMonthlyTotal(customerId, monthEnd1));
        Future<BigDecimal> month2 = Executors.newCachedThreadPool().submit(getMonthlyTotal(customerId, monthEnd2));

        ConcurrentHashMap<Date, PurchaseHistory> purchaseHistory = new ConcurrentHashMap<>();
        purchaseHistory.put(monthEnd1, new PurchaseHistory(monthEnd1, BigDecimal.ZERO));
        purchaseHistory.put(monthEnd2, new PurchaseHistory(monthEnd2, BigDecimal.ZERO));

        try {
            purchaseHistory.get(monthEnd1).setTotalPurchases(month1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            purchaseHistory.get(monthEnd2).setTotalPurchases(month2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(purchaseHistory.values());
    }

    public Observable<PurchaseHistory> getPurchaseHistoryAsync(long customerId, Date monthEnd1, Date monthEnd2) {

        Observable<Date> months = Observable.just(monthEnd1, monthEnd2);

        return months.flatMap(month->
                getMonthlyTotalAsync(customerId, month)
                .map(total->new PurchaseHistory(month, total))
                .toObservable());
    }

    private Single<BigDecimal> getMonthlyTotalAsync(long customerId, Date monthEnd) {
        return Single.create(source -> {
            try {
                Calendar monthStart = Calendar.getInstance();
                monthStart.setTime(monthEnd);
                monthStart.set(Calendar.DAY_OF_MONTH, 1);
                OrderModel[] orders = getOrderHistory(customerId, monthStart.getTime(), monthEnd);
                source.onSuccess(Arrays.stream(orders).map(o -> o.getTotal()).reduce((i1, i2) -> i1.add(i2)).orElse(BigDecimal.ZERO));
            } catch (Exception e) {
                logger.warn(String.format("Unable to get order history for month ending in: %s",monthEnd), e );
                source.onSuccess(BigDecimal.ZERO);
            }
        });
    }
}
