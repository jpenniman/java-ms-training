package com.northwind.customerservice.infrastructure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerFactoryImpl implements LoggerFactory {

    @Override
    public Log getLogger(String name) {
        return LogFactory.getLog(name);
    }

    @Override
    public Log getLogger(Class clazz) {
        return LogFactory.getLog(clazz);
    }
}
