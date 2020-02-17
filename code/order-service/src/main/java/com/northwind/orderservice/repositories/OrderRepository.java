package com.northwind.orderservice.repositories;

import com.northwind.orderservice.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByCustomerId(long customerId, Pageable pageable);
    List<Order> findByCustomerIdAndOrderDateBetween(long customerId, Date startDate, Date endDate);
    List<Order> findByCustomerNo(String customerNo);
}
