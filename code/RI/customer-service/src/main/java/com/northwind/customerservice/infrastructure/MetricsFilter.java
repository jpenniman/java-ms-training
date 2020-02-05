package com.northwind.customerservice.infrastructure;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class MetricsFilter implements Filter {
    MeterRegistry meterRegistry;

    // This method gets called once when Tomcat loads the filter (at startup)
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // We can use constructor injection with Servlet Filters,
        // so we use service location instead.
        // The Spring WebApplicationContextUtils gets us access to the
        // Spring IoC reference stored in the ServletContext.
        meterRegistry = (MeterRegistry) WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext())
                .getBean("meterRegistry");
    }

    // This method gets called for every request.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Count every request.
        meterRegistry.counter("request.count").increment();

        Instant start = Instant.now();
        try {
            // Call the next filter in the chain.
            // This method returns on response
            chain.doFilter(request, response);
        } catch (Exception ex) {
            // Count any exceptions that happen in the pipeline as a failure.
            // HTTP 500 errors don't happen here. Those are counted in the GlobalErrorHandler.
            meterRegistry.counter("request.failure").increment();

            // Rethrow to maintain original behavior (what would have happened if we didn't have a try/catch.)
            throw ex;
        } finally {
            // Regardless of success or failure, record the timing of the request.
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            meterRegistry.timer("request.latency").record(timeElapsed, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void destroy() {

    }
}
