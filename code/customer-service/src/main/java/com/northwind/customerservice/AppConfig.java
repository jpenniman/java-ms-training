package com.northwind.customerservice;

import com.northwind.customerservice.infrastructure.LoggerFactory;
import com.northwind.customerservice.infrastructure.LoggerFactoryImpl;
import com.northwind.customerservice.infrastructure.TraceContext;
import com.northwind.customerservice.repositories.CustomerRepository;
import com.northwind.customerservice.repositories.impl.AddressRowMapper;
import com.northwind.customerservice.repositories.impl.CustomerRowMapper;
import com.northwind.customerservice.repositories.impl.InMemoryCustomerRepository;
import com.northwind.customerservice.repositories.impl.MySqlCustomerRepository;
import com.northwind.customerservice.services.CustomerService;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdFlavor;
import io.micrometer.statsd.StatsdMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.time.Duration;

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
                                                 AddressRowMapper addressRowMapper,
                                                 LoggerFactory loggerFactory,
                                                 MeterRegistry meterRegistry,
                                                 TraceContext traceContext) {
        return new MySqlCustomerRepository(dataSource,
                customerRowMapper,
                addressRowMapper,
                loggerFactory,
                meterRegistry,
                traceContext);
    }

    @Bean
    public CustomerRowMapper customerRowMapper() {
        return new CustomerRowMapper();
    }

    @Bean
    public AddressRowMapper addressRowMapper() {
        return new AddressRowMapper();
    }

    @Bean
    public LoggerFactory loggerFactory() {
        return new LoggerFactoryImpl();
    }

    @Bean
    public MeterRegistry meterRegistry() {
        StatsdConfig statsdConfig = new StatsdConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public StatsdFlavor flavor() {
                return StatsdFlavor.DATADOG;
            }

            @Override
            public String host() {
                return "localhost";
            }

            @Override
            public String prefix() {
                return "com.northwind.customerservice";
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(1);
            }

            @Override
            public boolean enabled() {
                return true;
            }
        };

        MeterRegistry meterRegistry = new StatsdMeterRegistry(statsdConfig, Clock.SYSTEM);

        meterRegistry.config().commonTags("service","customer-service");
        new ClassLoaderMetrics().bindTo(meterRegistry);
        new JvmMemoryMetrics().bindTo(meterRegistry);
        new JvmGcMetrics().bindTo(meterRegistry);
        new ProcessorMetrics().bindTo(meterRegistry);
        new JvmThreadMetrics().bindTo(meterRegistry);
        return meterRegistry;
    }

    @Bean
    @RequestScope
    public TraceContext traceContext() {
        return new TraceContext();
    }


}









