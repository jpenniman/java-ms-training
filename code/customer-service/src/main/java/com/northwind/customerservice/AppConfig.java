package com.northwind.customerservice;

import com.northwind.customerservice.repositories.CustomerRepository;
import com.northwind.customerservice.repositories.impl.InMemoryCustomerRepository;
import com.northwind.customerservice.services.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.northwind.customerservice"})
public class AppConfig {

    //DI Configuration goes here.
    @Bean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }
}