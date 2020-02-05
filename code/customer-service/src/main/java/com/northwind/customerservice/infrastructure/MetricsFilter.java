package com.northwind.customerservice.infrastructure;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class MetricsFilter implements Filter {

    MeterRegistry meterRegistry;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        meterRegistry = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext())
                .getBean(MeterRegistry.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        meterRegistry.counter("request.count").increment();

        Instant startTime = Instant.now();
        try {
            // Code here happens on request...

            // call the next filter
            chain.doFilter(request, response);

            //code here happens on response...

        } catch (Exception ex) {
            meterRegistry.counter("request.failure").increment();
            throw ex;
        } finally {
            Instant stopTime = Instant.now();
            meterRegistry.timer("request.latency")
                    .record(Duration.between(startTime, stopTime).toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void destroy() {

    }
}
