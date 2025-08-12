package com.coindesk.demo.coindesk.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDeskInfoResponse {
    @JsonProperty("time")
    private CoinDeskTimeInfoResponse time;
    @JsonProperty("disclaimer")
    private String disclaimer;
    @JsonProperty("chartName")
    private String chartName;
    @JsonProperty("bpi")
    private Map<String, CoinDeskDetailResponse> currencies;

    public CoinDeskDetailResponse getDetailByCurrency(String currency) {
        if (ObjectUtils.isEmpty(this)) return null;
        Map<String, CoinDeskDetailResponse> currencies = this.getCurrencies();
        if (ObjectUtils.isEmpty(currencies)) return null;
        return currencies.get(currency);
    }
}
