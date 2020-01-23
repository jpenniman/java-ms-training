package com.northwind.loggingservice.providers;

public interface LoggingProvider {

    void sendEvent(LoggingEvent event) throws LoggingProviderException;
}
