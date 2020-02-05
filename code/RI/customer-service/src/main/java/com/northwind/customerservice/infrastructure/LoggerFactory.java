package com.northwind.customerservice.infrastructure;

import org.apache.commons.logging.Log;

// JCL LogFactory is a static class and therefor no injectable.
// We create our own loggetrFactory to wrap the JCL factor so we
// can inject our factory for unit tests.

public interface LoggerFactory {
    Log getLogger(String name);
    Log getLogger(Class clazz);
}
