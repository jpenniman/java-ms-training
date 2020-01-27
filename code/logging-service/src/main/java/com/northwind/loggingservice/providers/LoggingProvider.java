package com.northwind.loggingservice.providers;

import java.util.List;

public interface LoggingProvider {

    void sendEvent(LoggingEvent event) throws LoggingProviderException;
    void sendEvent(List<LoggingEvent> event) throws LoggingProviderException;
}
