package com.northwind.orderservice.infrastructure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JclLoggerFactory implements LoggerFactory {
    @Override
    public Log getLogger(Class clazz) {
        return LogFactory.getLog(clazz);
    }

    @Override
    public Log getLogger(String name) {
        return LogFactory.getLog(name);
    }
}
