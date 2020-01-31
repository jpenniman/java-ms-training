package com.northwind.customerservice;

import com.northwind.customerservice.repositories.CustomerRepository;
import com.northwind.customerservice.repositories.impl.AddressRowMapper;
import com.northwind.customerservice.repositories.impl.CustomerRowMapper;
import com.northwind.customerservice.repositories.impl.InMemoryCustomerRepository;
import com.northwind.customerservice.repositories.impl.MySqlCustomerRepository;
import com.northwind.customerservice.services.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.northwind.customerservice"})
public class AppConfig {

    @Bean
    public DataSource datasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/customers-db");
        dataSource.setUsername("customers-user");
        dataSource.setPassword("password");

        return dataSource;
    }
    //DI Configuration goes here.
    @Bean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }

    @Bean
    public CustomerRepository customerRepository(DataSource dataSource,
                                                 CustomerRowMapper customerRowMapper,
                                                 AddressRowMapper addressRowMapper) {
        return new MySqlCustomerRepository(dataSource, customerRowMapper, addressRowMapper);
    }

    @Bean
    public CustomerRowMapper customerRowMapper() {
        return new CustomerRowMapper();
    }

    @Bean
    public AddressRowMapper addressRowMapper() {
        return new AddressRowMapper();
    }
}