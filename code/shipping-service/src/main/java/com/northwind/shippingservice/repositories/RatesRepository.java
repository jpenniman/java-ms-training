package com.northwind.shippingservice.repositories;

import com.northwind.shippingservice.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRepository extends JpaRepository<Rate, Long> {
    Rate findByCountry(String country);
}
