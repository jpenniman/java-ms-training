package com.northwind.loggingservice.providers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonProperty
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date timestamp;
    @JsonProperty
    private LogLevel level;
    @JsonProperty("thread")
    private String threadName;
    @JsonProperty("logger")
    private String category;
    @JsonProperty
    private String message;
    @JsonProperty
    private Map<String, String> mdc = new HashMap<>();


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

    public Map<String, String> getMdc() {
        return mdc;
    }
}
