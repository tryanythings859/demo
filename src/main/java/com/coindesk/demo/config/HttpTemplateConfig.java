package com.coindesk.demo.config;

import com.coindesk.demo.handler.ExternalServiceErrorHandler;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Configuration
public class HttpTemplateConfig {

    private static final int TIME_OUT = 30;
    private final int poolNum;
    private final int keepAlive;

    public HttpTemplateConfig(@Value("${rest.connection.pool:600}") int poolNum,
                              @Value("${rest.keep.alive:300}") int keepAlive) {
        this.poolNum = poolNum;
        this.keepAlive = keepAlive;
    }

    @Qualifier("cloudRestTemplate")
    @Bean(name = "cloudRestTemplate")
    public RestTemplate cloudRestTemplate() {
        return createRestTemplate(new ExternalServiceErrorHandler());
    }

    private RestTemplate createRestTemplate(ResponseErrorHandler errorHandler) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .connectionPool(new ConnectionPool(poolNum, keepAlive, TimeUnit.SECONDS))
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
