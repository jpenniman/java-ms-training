package com.northwind.customerservice.infrastructure;

import org.apache.commons.logging.Log;

public interface LoggerFactory {
    Log getLog(String name);
    Log getLog(Class clazz);
}
