package com.coindesk.demo.coindesk.dto.response;

import com.coindesk.demo.coindesk.dto.converter.InstantNewUTCConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewCoinDeskInfoResponse {
    @JsonSerialize(using = InstantNewUTCConverter.serializer.class)
    @JsonProperty("updated")
    private Instant updated;
    @JsonProperty("disclaimer")
    private String disclaimer;
    @JsonProperty("chartName")
    private String chartName;
    @JsonProperty("bpi")
    private Map<String, NewCoinDeskDetailResponse> currencies;
}
