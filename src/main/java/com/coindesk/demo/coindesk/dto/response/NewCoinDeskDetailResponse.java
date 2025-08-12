package com.coindesk.demo.coindesk.dto.response;

import com.coindesk.demo.coindesk.dto.converter.CurrencyNameConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewCoinDeskDetailResponse {
    @JsonSerialize(using = CurrencyNameConverter.serializer.class)
    @JsonProperty("zhCode")
    private String zhCode;
    @JsonProperty("code")
    private String code;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("rate")
    private String rate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("rate_float")
    private BigDecimal rateFloat;
}

