package com.northwind.customerservice.infrastructure;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TracingRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private ApplicationContext container;

    public TracingRestTemplateInterceptor(ApplicationContext container) {
        this.container = container;
    }
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        TraceContext traceContext = container.getBean(TraceContext.class);
        request.getHeaders().add(TraceContext.TRACE_ID_HEADER, traceContext.getTraceId());
        return execution.execute(request, body);
    }
}
