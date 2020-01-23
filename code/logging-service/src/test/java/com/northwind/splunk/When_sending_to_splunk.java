package com.northwind.splunk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class When_sending_to_splunk {

    @Test
    public void given_a_valid_event_write_should_succeed() {

        SplunkClient client = new SplunkClient();

        SplunkRequest request = new SplunkRequest("test");

        SplunkResponse response = client.send(request);

        Assertions.assertTrue(response.isSuccess());
    }
}
