package com.northwind.loggingservice.providers;

import com.northwind.splunk.SplunkClient;
import com.northwind.splunk.SplunkRequest;
import com.northwind.splunk.SplunkResponse;

import java.util.ArrayList;
import java.util.List;

public class SplunkLoggingProvider implements LoggingProvider {

    private SplunkClient splunk;

    public SplunkLoggingProvider(SplunkClient splunk) {
        this.splunk = splunk;
    }

    @Override
    public void sendEvent(LoggingEvent event) throws LoggingProviderException {

        try {
            SplunkRequest request = new SplunkRequest(event);

            SplunkResponse response = splunk.send(request);

            if (!response.isSuccess()) {
                throw new LoggingProviderException(response);
            }
        } catch (LoggingProviderException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new LoggingProviderException(ex);
        }
    }

    @Override
    public void sendEvent(List<LoggingEvent> event) throws LoggingProviderException {
        List<SplunkRequest> requests = new ArrayList<>();

        for(LoggingEvent evt : event) {
            requests.add(new SplunkRequest(evt));
        }

        try {
            SplunkResponse response = splunk.send(requests);

            if (!response.isSuccess()) {
                throw new LoggingProviderException(response);
            }
        } catch (LoggingProviderException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new LoggingProviderException(ex);
        }
    }
}
