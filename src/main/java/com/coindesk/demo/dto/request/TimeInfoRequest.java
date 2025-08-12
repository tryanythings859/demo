package com.coindesk.demo.dto.request;

import com.coindesk.demo.coindesk.dto.converter.InstantBSTConverter;
import com.coindesk.demo.coindesk.dto.converter.InstantISOConverter;
import com.coindesk.demo.coindesk.dto.converter.InstantUTCConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.Instant;

@Data
public class TimeInfoRequest {
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

