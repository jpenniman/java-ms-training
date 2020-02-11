package com.northwind.orderservice.infrastructure;

import org.apache.commons.logging.Log;

public interface LoggerFactory {
    Log getLogger(Class clazz);
    Log getLogger(String name);
}
