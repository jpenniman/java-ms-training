package com.northwind.splunk;

import com.northwind.loggingservice.providers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class When_using_the_SplunkLoggingProvider {

    @Test
    public void given_valid_input_op_should_succeed() {

        SplunkClient client = new SplunkClientMock();
        LoggingProvider loggingProvider = new SplunkLoggingProvider(client);
        LoggingEvent event = new LoggingEvent();
        event.setMessage("test");
        event.setLevel(LogLevel.INFO);
        loggingProvider.sendEvent(event);
    }

    @Test
    public void error_response_should_throw_ex() {
        Assertions.assertThrows(LoggingProviderException.class, ()->{
            SplunkClient client = new SplunkClientFailureMock();
            LoggingProvider loggingProvider = new SplunkLoggingProvider(client);
            LoggingEvent event = new LoggingEvent();
            event.setMessage("test");
            event.setLevel(LogLevel.INFO);
            loggingProvider.sendEvent(event);
        });
    }
    @Test
    public void exception_should_throw_ex() {
        Assertions.assertThrows(LoggingProviderException.class, ()->{
            SplunkClient client = new SplunkClient() {
                @Override
                public SplunkResponse send(SplunkRequest request) {
                    throw new RuntimeException("test");
                }
            };

            LoggingProvider loggingProvider = new SplunkLoggingProvider(client);
            LoggingEvent event = new LoggingEvent();
            event.setMessage("test");
            event.setLevel(LogLevel.INFO);
            loggingProvider.sendEvent(event);
        });
    }
}
