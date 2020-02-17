package com.northwind.customerservice.infrastructure;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestTemplateFactoryImpl implements RestTemplateFactory {

    @Autowired
    private ApplicationContext applicationContext;

    private RestTemplateFactoryImpl() {}

    public final static RestTemplateFactory INSTANCE = new RestTemplateFactoryImpl();

    private final ConcurrentHashMap<String, RestTemplate> instances =
            new ConcurrentHashMap<String, RestTemplate>();

    public RestTemplate getInstance(String name) {

        if (!instances.containsKey(name)) {
            synchronized (instances) {
                if (!instances.containsKey(name)) {
                    RestTemplate restTemplate = null;
                    if (isProduction()) {
                        restTemplate = new RestTemplate();
                    } else {
                        // HACK: NEVER NEVER NEVER NEVER NEVER DO THIS IN PRODUCTION
                        restTemplate = GetInSecureSslRestTemplate(30000);
                    }
                    instances.put(name, restTemplate);
                }
            }
        }

        return instances.get(name);
    }

    private boolean isProduction() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("DEPLOY_ENV"))
            return env.get("DEPLOY_ENV").equalsIgnoreCase("PROD");
        else
            return false;
    }

    private RestTemplate GetInSecureSslRestTemplate(int connectionRequestTimeoutInMs) {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeoutInMs)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();


        requestFactory.setHttpClient(httpClient);

        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.additionalInterceptors(new TracingRestTemplateInterceptor(applicationContext));
        builder.requestFactory(()->requestFactory);

        return builder.build();
    }
}
