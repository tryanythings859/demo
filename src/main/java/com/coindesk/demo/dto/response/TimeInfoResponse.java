package com.coindesk.demo.dto.response;

import com.coindesk.demo.coindesk.dto.converter.InstantBSTConverter;
import com.coindesk.demo.coindesk.dto.converter.InstantISOConverter;
import com.coindesk.demo.coindesk.dto.converter.InstantUTCConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.Instant;

@Data
public class TimeInfoResponse {
    @JsonSerialize(using = InstantUTCConverter.serializer.class)
    @JsonProperty("updated")
    private Instant updated;
    @JsonSerialize(using = InstantISOConverter.serializer.class)
    @JsonProperty("updatedISO")
    private Instant updatedISO;
    @JsonSerialize(using = InstantBSTConverter.serializer.class)
    @JsonProperty("updateduk")
    private Instant updatedUK;
}

