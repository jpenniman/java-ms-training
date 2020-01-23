package com.northwind.splunk;

public class SplunkResponse {
    // Mark all fields final to make the object immutable.
    // Final means their value can't be changed once initialized.
    private final String text;
    private final int code;

    public SplunkResponse(String text, int code) {
        // Final fields can only be set at construction time
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        // We know from the Splunk documentation that a code of 0 is a success.
        return code == 0;
    }
}
