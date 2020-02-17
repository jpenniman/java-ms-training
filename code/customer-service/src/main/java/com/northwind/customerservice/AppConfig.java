package com.northwind.customerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.customerservice.infrastructure.*;
import com.northwind.customerservice.proxies.OrderClientConfig;
import com.northwind.customerservice.proxies.OrderServiceClient;
import com.northwind.customerservice.proxies.impl.OrderServiceClientImpl;
import com.northwind.customerservice.repositories.CustomerRepository;
import com.northwind.customerservice.repositories.impl.AddressRowMapper;
import com.northwind.customerservice.repositories.impl.CustomerRowMapper;
import com.northwind.customerservice.repositories.impl.MySqlCustomerRepository;
import com.northwind.customerservice.services.CustomerService;
import com.northwind.customerservice.services.OrderHistoryService;
//import com.northwind.framework.RestTemplateFactory;
//import com.northwind.framework.RestTemplateFactoryImpl;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdMeterRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableSwagger2
@EnableWebSecurity
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.northwind.customerservice"})
public class AppConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    private Properties properties = new Properties();
    public AppConfig() {
        try {
            InputStream appPropsFile = AppConfig.class.getClassLoader()
                    .getResourceAsStream("application.properties");
            properties.load(appPropsFile);
            appPropsFile.close();
        } catch (IOException ex) {

        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TracingHandlerInterceptor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        MappingJackson2HttpMessageConverter jsonConverter =
                new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);

        converters.add(jsonConverter);
    }

    // Create 2 users for demo
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("efudd")
                        .password(passwordEncoder().encode("password")).roles("USER")
                .and()
                .withUser("hsimpson")
                        .password(passwordEncoder().encode("password")).roles("USER", "ADMIN");

    }

    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/customers/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/addresses/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/customers").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/customers/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/customers/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/customers/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfo(
                        "Customer API",
                        "APIs for working with Customers in the Sales Order System.",
                        "v1",
                        "https://northwind.com/apis/termsofservice",
                        new Contact("Northwind Support", "https://northwind.com/support", "support@northwind.com"),
                        "End User License Agreement",
                        "https://northwind.com/apis/eula",
                        Collections.EMPTY_LIST));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public DataSource datasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(properties.getProperty("db.connectionString"));
        dataSource.setUsername(properties.getProperty("db.username"));
        dataSource.setPassword(properties.getProperty("db.password"));

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
                return properties.getProperty(key);
            }
            @Override
            public String prefix() {
                return "metrics";
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

    @Bean
    public RestTemplateFactory restTemplateFactory() {
        return RestTemplateFactoryImpl.INSTANCE;
    }

    @Bean
    public OrderClientConfig orderClientConfig() {
        OrderClientConfig config = new OrderClientConfig();
        config.setUrl(properties.getProperty("order-service.url"));
        return config;
    }

    @Bean
    public OrderServiceClient orderServiceClient(OrderClientConfig orderClientConfig, RestTemplateFactory restTemplateFactory) {
        return new OrderServiceClientImpl(orderClientConfig, restTemplateFactory);
    }
    @Bean
    public OrderHistoryService orderHistoryService(OrderServiceClient orderServiceClient, LoggerFactory loggerFactory) {
            return new OrderHistoryService(orderServiceClient, loggerFactory);
    }
    @Bean
    public FormattingConversionService conversionService() {
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ISO_DATE);
        registrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME);
        registrar.registerFormatters(conversionService);

        return conversionService;
    }

    @Bean
    public LoggingAspect loggingAspect(){
        return new LoggingAspect();
    }

}









