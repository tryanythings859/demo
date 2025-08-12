package com.coindesk.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ExternalServiceErrorHandler implements ResponseErrorHandler {

    private final ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String responseBody = convertResponseBodyToString(response);
        log.error("RestTemplate Call API Error. Http Status Code : " + response.getStatusCode().value() + ", Response : " + responseBody);
    }

    private String convertResponseBodyToString(ClientHttpResponse response) {
        return new String(getResponseBody(response), StandardCharsets.UTF_8);
    }


    private byte[] getResponseBody(ClientHttpResponse response) {
        try {
            return FileCopyUtils.copyToByteArray(response.getBody());
        } catch (IOException var3) {
            return new byte[0];
        }
    }

}