package com.coindesk.demo.dto.mapper;

import com.coindesk.demo.dto.response.AuditTimestampsResponse;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.dto.response.TimeInfoResponse;
import com.coindesk.demo.entity.AuditTimestamps;
import com.coindesk.demo.entity.CurrencyDetailEntity;
import com.coindesk.demo.entity.CurrencyInfoEntity;
import com.coindesk.demo.entity.TimeInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface CurrencyInfoEntityMapper {
    CurrencyInfoEntityMapper INSTANCE = Mappers.getMapper(CurrencyInfoEntityMapper.class);

    @Mappings({
            @Mapping(target = "currencies", source = "currencies"),
            @Mapping(target = "time", source = "timeInfo"),
            @Mapping(target = "auditTimestamps", source = "auditTimestamps")
    })
    CurrencyInfoResponse toResponse(CurrencyInfoEntity entity);

    @Mappings({
            @Mapping(target = "updated", source = "updated"),
            @Mapping(target = "updatedISO", source = "updatedISO"),
            @Mapping(target = "updatedUK", source = "updatedUK")
    })
    TimeInfoResponse toTimeInfoResponse(TimeInfoEntity entity);

    Map<String, CurrencyDetailResponse> toDetailResponseMap(Map<String, CurrencyDetailEntity> source);

    @Mappings({
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "symbol", source = "symbol"),
            @Mapping(target = "rate", source = "rate"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "rateFloat", source = "rateFloat")
    })
    CurrencyDetailResponse toDetailResponse(CurrencyDetailEntity entity);

    @Mappings({
            @Mapping(target = "updatedAt", source = "updatedAt"),
    })
    AuditTimestampsResponse toAuditTimestampResponse(AuditTimestamps entity);
}
