package com.northwind.loggingservice;

import com.northwind.loggingservice.providers.LoggingProvider;
import com.northwind.loggingservice.providers.SplunkLoggingProvider;
import com.northwind.splunk.SplunkClient;
import com.northwind.splunk.SplunkClientImpl;
import com.northwind.loggingservice.workers.LoggingWorker;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        try {
            config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SplunkClient splunkClient = new SplunkClientImpl(config.getSplunkConfig());
        LoggingProvider provider = new SplunkLoggingProvider(splunkClient);
        LoggingWorker worker = new LoggingWorker(provider,
                                                 config.getServiceConfig(),
                                                 config.getQueueConfig());

        Thread workerThread = new Thread(worker);
        workerThread.start();
    }
}
