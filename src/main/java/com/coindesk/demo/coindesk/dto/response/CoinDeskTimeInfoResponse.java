package com.coindesk.demo.coindesk.dto.response;

import com.coindesk.demo.coindesk.dto.converter.InstantBSTConverter;
import com.coindesk.demo.coindesk.dto.converter.InstantISOConverter;
import com.coindesk.demo.coindesk.dto.converter.InstantUTCConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDeskTimeInfoResponse {
    @JsonDeserialize(using = InstantUTCConverter.deserializer.class)
    @JsonProperty("updated")
    private Instant updated;
    @JsonDeserialize(using = InstantISOConverter.deserializer.class)
    @JsonProperty("updatedISO")
    private Instant updatedISO;
    @JsonDeserialize(using = InstantBSTConverter.deserializer.class)
    @JsonProperty("updateduk")
    private Instant updatedUK;
}

