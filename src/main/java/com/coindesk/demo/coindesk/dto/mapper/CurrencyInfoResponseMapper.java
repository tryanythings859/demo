package com.coindesk.demo.coindesk.dto.mapper;

import com.coindesk.demo.coindesk.dto.response.CoinDeskDetailResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskInfoResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskTimeInfoResponse;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.dto.response.TimeInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CurrencyInfoResponseMapper {
    CoinDeskInfoResponse INSTANCE = Mappers.getMapper(CoinDeskInfoResponse.class);

    @Mappings({
            @Mapping(target = "currencies", source = "currencies"),
            @Mapping(target = "time", source = "time")
    })
    CurrencyInfoResponse toResponse(CoinDeskInfoResponse response);

    @Mappings({
            @Mapping(target = "updated", source = "updated"),
            @Mapping(target = "updatedISO", source = "updatedISO"),
            @Mapping(target = "updatedUK", source = "updatedUK")
    })
    TimeInfoResponse toTimeInfoResponse(CoinDeskTimeInfoResponse response);

    @Mappings({
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "symbol", source = "symbol"),
            @Mapping(target = "rate", source = "rate"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "rateFloat", source = "rateFloat")
    })
    CurrencyDetailResponse toDetailResponse(CoinDeskDetailResponse response);

}
