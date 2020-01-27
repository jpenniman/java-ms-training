package com.northwind.loggingservice;

import com.northwind.loggingservice.providers.LoggingProvider;
import com.northwind.loggingservice.providers.SplunkLoggingProvider;
import com.northwind.splunk.SplunkClient;
import com.northwind.splunk.SplunkClientImpl;
import com.northwind.workers.LoggingWorker;

public class Application {

    public static void main(String[] args) {
        SplunkClient splunkClient = new SplunkClientImpl();
        LoggingProvider provider = new SplunkLoggingProvider(splunkClient);
        LoggingWorker worker = new LoggingWorker(provider);

        Thread workerThread = new Thread(worker);
        workerThread.start();
    }
}
