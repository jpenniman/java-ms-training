package com.northwind.splunk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.framework.RestTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SplunkClientImpl implements SplunkClient {

    private final RestTemplateFactory restTemplateFactory =
            RestTemplateFactory.INSTANCE;

    private final HttpHeaders headers;
    private final ObjectMapper objectMapper;
    private final SplunkConfig config;
    private final Logger logger;
    private final String url;

    public SplunkClientImpl(SplunkConfig config) {
        this.config = config;
        this.logger = LoggerFactory.getLogger(SplunkClientImpl.class);
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        headers.add("Authorization", String.format("Splunk %s", config.getToken()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        url = String.format("%s/services/collector/event", config.getServer());
    }

    @Override
    public SplunkResponse send(SplunkRequest request) {
        try {
            RestTemplate client = restTemplateFactory.getInstance("splunk");

            HttpEntity<SplunkRequest> httpEntity = new HttpEntity<>(request, headers);

            return client.postForObject(url, httpEntity, SplunkResponse.class);
        } catch (Exception e) {
            logger.debug("Error sending to splunk", e);
            throw e;
        }
    }

    @Override
    public SplunkResponse send(List<SplunkRequest> request) {
        try {
            RestTemplate client = restTemplateFactory.getInstance("splunk");

            StringBuilder sb = new StringBuilder();

            request.stream().forEach(req -> {
                try {
                    String json = objectMapper.writeValueAsString(req);
                    sb.append(json);
                } catch (JsonProcessingException e) {
                    // It's best practice to log at the DEBUG level here, then rethrow the exception.
                    logger.debug("Error serializing SplunkRequest", e);
                }
            });

            HttpEntity<String> httpEntity = new HttpEntity<>(sb.toString(), headers);

            return client.postForObject(url, httpEntity, SplunkResponse.class);
        } catch (Exception e) {
            // It's best practice to log at the DEBUG level here, then rethrow the exception.
            logger.debug("Error sending to splunk", e);
            throw e;
        }
    }
}
