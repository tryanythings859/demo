package com.coindesk.demo.util.http;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public interface HttpTemplateStrategy {

    RestTemplate getRestTemplate();

    String getHost();

    default <RE, RP> ResponseEntity<RP> doRequest(HttpTemplateBean<RE, RP> httpBean) {
        return doRequest(getRestTemplate(), httpBean);
    }

    default <RE, RP> ResponseEntity<RP> doRequest(RestTemplate restTemplate, HttpTemplateBean<RE, RP> httpBean) {
        return doRequest(restTemplate, httpBean, getHost());
    }

    default <RE, RP> ResponseEntity<RP> doRequest(RestTemplate restTemplate, HttpTemplateBean<RE, RP> httpBean, String domain) {
        return doRequest(restTemplate, httpBean.convertToHttpTemplate(domain));
    }

    default <RE, RP> ResponseEntity<RP> doRequest(RestTemplate restTemplate, HttpTemplate<RE, RP> httpBean) {
        if (!Objects.isNull(httpBean.getPathVariable())) {
            return restTemplate.exchange(httpBean.getUrl(),
                    httpBean.getHttpMethod(),
                    httpBean.getHttpEntity(),
                    httpBean.getResponse(),
                    httpBean.getPathVariable());
        }
        return restTemplate.exchange(httpBean.getUrl(),
                httpBean.getHttpMethod(),
                httpBean.getHttpEntity(),
                httpBean.getResponse());
    }
}
