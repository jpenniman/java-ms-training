package com.northwind.loggingservice.providers;

public class LoggingProviderException extends RuntimeException {

    private Object response;

    public LoggingProviderException(Object response) {
        this.response = response;
    }

    public LoggingProviderException(Exception cause) {
        super(cause);
    }

    public Object getResponse() {
        return response;
    }
}
