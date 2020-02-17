package com.northwind.customerservice.infrastructure;

import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TracingHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ApplicationContext container = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        try {
            TraceContext traceContext = container.getBean(TraceContext.class);
            String traceIdHeader = request.getHeader(TraceContext.TRACE_ID_HEADER);
            if (traceIdHeader != null && traceIdHeader.trim().length() != 0) {
                traceContext.setTraceId(traceIdHeader);
            }
            MDC.put(TraceContext.TRACE_ID_HEADER, traceContext.getTraceId());
        } catch (Exception ex) {
            //Don't blow up the interceptor. Tracing isn't THAT critical to the success of the request
            LogFactory.getLog(TracingHandlerInterceptor.class).warn("An error occurred while trying to set the trace id.", ex);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
