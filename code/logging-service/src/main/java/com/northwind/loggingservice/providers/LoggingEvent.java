package com.northwind.loggingservice.providers;

import java.util.Date;

public class LoggingEvent {
/*
{
  "timestamp" : "2020-01-22 08:24:34.975",
  "level" : "INFO",
  "thread" : "main",
  "logger" : "test",
  "message" : "This is a test",
  "context" : "default"
}
*/

    private Date timestamp;
    private LogLevel level;
    private String threadName;
    private String category;
    private String message;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
