package com.coindesk.demo.coindesk.dto.mapper;

import com.coindesk.demo.coindesk.dto.response.CoinDeskDetailResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskInfoResponse;
import com.coindesk.demo.coindesk.dto.response.NewCoinDeskDetailResponse;
import com.coindesk.demo.coindesk.dto.response.NewCoinDeskInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CurrencyInfoNewResponseMapper {
    CoinDeskInfoResponse INSTANCE = Mappers.getMapper(CoinDeskInfoResponse.class);

    @Mappings({
            @Mapping(target = "currencies", source = "currencies"),
            @Mapping(target = "updated", source = "time.updated")
    })
    NewCoinDeskInfoResponse toResponse(CoinDeskInfoResponse response);


    @Mappings({
            @Mapping(target = "zhCode", source = "code"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "symbol", source = "symbol"),
            @Mapping(target = "rate", source = "rate"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "rateFloat", source = "rateFloat")
    })
    NewCoinDeskDetailResponse toDetailResponse(CoinDeskDetailResponse response);

}
