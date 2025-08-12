package com.coindesk.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyInfoResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("time")
    private TimeInfoResponse time;
    @JsonProperty("disclaimer")
    private String disclaimer;
    @JsonProperty("chartName")
    private String chartName;
    @JsonUnwrapped
    private AuditTimestampsResponse auditTimestamps;
    @JsonProperty("bpi")
    private Map<String, CurrencyDetailResponse> currencies;

    public CurrencyDetailResponse getDetailByCurrency(String currency) {
        if (ObjectUtils.isEmpty(this)) return null;
        Map<String, CurrencyDetailResponse> currencies = this.getCurrencies();
        if (ObjectUtils.isEmpty(currencies)) return null;
        return currencies.get(currency);
    }
}
