package com.coindesk.demo.util.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Map;

public interface HttpTemplate<RE, RP> {
    HttpMethod getHttpMethod();

    String getDomain();

    String getUri();

    default String getUrl() {
        return getDomain() + getUri();
    }

    default RE getRequestBody() {
        return null;
    }

    default MultiValueMap<String, String> getQueryParam() {
        return null;
    }

    default Map<String, String> getPathVariable() {
        return Collections.emptyMap();
    }

    Class<RP> getResponse();

    default HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        return httpHeaders;
    }

    default HttpEntity<RE> getHttpEntity() {
        return new HttpEntity<>(getRequestBody(), getHttpHeaders());
    }
}