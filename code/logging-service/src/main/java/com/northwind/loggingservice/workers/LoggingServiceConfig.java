package com.northwind.loggingservice.workers;

public class LoggingServiceConfig {
    private int bufferSize;
    private long flushIntervalInSeconds;

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public long getFlushIntervalInSeconds() {
        return flushIntervalInSeconds;
    }

    public void setFlushIntervalInSeconds(long flushIntervalInSeconds) {
        this.flushIntervalInSeconds = flushIntervalInSeconds;
    }
}
