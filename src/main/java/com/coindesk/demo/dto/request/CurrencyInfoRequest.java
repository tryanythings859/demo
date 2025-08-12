package com.coindesk.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class CurrencyInfoRequest {
    @JsonProperty("time")
    private TimeInfoRequest time;
    @JsonProperty("disclaimer")
    private String disclaimer;
    @JsonProperty("chartName")
    private String chartName;
    @JsonProperty("bpi")
    private Map<String, CurrencyDetailRequest> currencies;
}
