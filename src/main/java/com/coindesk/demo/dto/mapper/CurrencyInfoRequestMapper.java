package com.coindesk.demo.dto.mapper;

import com.coindesk.demo.dto.request.CurrencyDetailRequest;
import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.TimeInfoRequest;
import com.coindesk.demo.entity.CurrencyDetailEntity;
import com.coindesk.demo.entity.TimeInfoEntity;
import com.coindesk.demo.entity.CurrencyInfoEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface CurrencyInfoRequestMapper {
    CurrencyInfoRequestMapper INSTANCE = Mappers.getMapper(CurrencyInfoRequestMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "auditTimestamps", ignore = true),
            @Mapping(target = "currencies", source = "currencies"),
            @Mapping(target = "timeInfo", source = "time")
    })
    CurrencyInfoEntity toEntity(CurrencyInfoRequest request);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "currencyInfo", ignore = true),
            @Mapping(target = "updated", source = "updated"),
            @Mapping(target = "updatedISO", source = "updatedISO"),
            @Mapping(target = "updatedUK", source = "updatedUK")
    })
    TimeInfoEntity toTimeInfoEntity(TimeInfoRequest request);

    Map<String, CurrencyDetailEntity> toPriceDetailEntityMap(Map<String, CurrencyDetailRequest> source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "currencyInfo", ignore = true),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "symbol", source = "symbol"),
            @Mapping(target = "rate", source = "rate"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "rateFloat", source = "rateFloat")
    })
    CurrencyDetailEntity toDetailEntity(CurrencyDetailRequest request);

    @AfterMapping
    default void linkParent(@MappingTarget CurrencyInfoEntity target) {
        if (target.getCurrencies() != null) {
            target.getCurrencies().values().forEach(d -> d.setInfo(target));
        }
    }
}
