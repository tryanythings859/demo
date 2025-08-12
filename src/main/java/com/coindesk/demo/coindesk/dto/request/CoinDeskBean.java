package com.coindesk.demo.coindesk.dto.request;

import com.coindesk.demo.coindesk.dto.response.CoinDeskInfoResponse;
import com.coindesk.demo.util.http.HttpTemplate;
import com.coindesk.demo.util.http.HttpTemplateBean;
import lombok.Getter;
import org.springframework.http.HttpMethod;

public class CoinDeskBean implements HttpTemplateBean<Void, CoinDeskInfoResponse> {

    @Override
    public HttpTemplate<Void, CoinDeskInfoResponse> convertToHttpTemplate(String domain) {
        return new Request<>(domain, null);
    }

    public static class Request<RE extends Void, RP extends CoinDeskInfoResponse> implements HttpTemplate<RE, RP> {
        @Getter
        private HttpMethod httpMethod = HttpMethod.GET;
        @Getter
        private final Class<RP> response = (Class<RP>) CoinDeskInfoResponse.class;
        @Getter
        private String uri = "/blog/coindesk.json";
        @Getter
        private final String domain;
        private final RE bean;

        public Request(String domain, RE bean) {
            this.domain = domain;
            this.bean = bean;
        }

    }
}
