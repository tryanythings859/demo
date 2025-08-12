package com.coindesk.demo.coindesk.config;

import com.coindesk.demo.util.http.HttpTemplateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CoinDeskHttpTemplate implements HttpTemplateStrategy {
    private final RestTemplate httpTemplate;
    private final String host;

    public CoinDeskHttpTemplate(@Qualifier("cloudRestTemplate") RestTemplate httpTemplate,
                                @Value("${coindesk.host:http://localhost}") String host) {
        this.httpTemplate = httpTemplate;
        this.host = host;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return httpTemplate;
    }

    @Override
    public String getHost() {
        return host;
    }

}
