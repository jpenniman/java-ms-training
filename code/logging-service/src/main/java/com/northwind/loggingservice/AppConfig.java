package com.northwind.loggingservice;

import com.northwind.loggingservice.workers.LoggingServiceConfig;
import com.northwind.loggingservice.workers.QueueConfig;
import com.northwind.splunk.SplunkConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    Properties properties = new Properties();

    public SplunkConfig getSplunkConfig(){
        SplunkConfig config = new SplunkConfig();
        config.setServer(properties.getProperty("splunk.server"));
        config.setToken(properties.getProperty("splunk.token"));
        return config;
    }

    public QueueConfig getQueueConfig() {
        QueueConfig config = new QueueConfig();
        config.setServer(properties.getProperty("rabbitmq.server"));
        config.setUsername(properties.getProperty("rabbitmq.username"));
        config.setPassword(properties.getProperty("rabbitmq.password"));
        config.setQueuename(properties.getProperty("rabbitmq.queuename"));
        return config;
    }

    public LoggingServiceConfig getServiceConfig() {
        LoggingServiceConfig config = new LoggingServiceConfig();
        config.setBufferSize(Integer.parseInt(properties.getProperty("logging-service.buffersize")));
        config.setFlushIntervalInSeconds(Long.parseLong(properties.getProperty("logging-service.flushtimeinseconds")));
        return config;
    }

    public void load() throws IOException {

        InputStream propertiesFileStream =
                AppConfig.class.getClassLoader()
                               .getResourceAsStream("application.properties");
        properties.load(propertiesFileStream);
        propertiesFileStream.close();
    }
}
