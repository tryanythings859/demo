package com.coindesk.demo.dto.mapper;

import com.coindesk.demo.dto.response.AuditTimestampsResponse;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.dto.response.TimeInfoResponse;
import com.coindesk.demo.entity.AuditTimestamps;
import com.coindesk.demo.entity.CurrencyDetailEntity;
import com.coindesk.demo.entity.CurrencyInfoEntity;
import com.coindesk.demo.entity.TimeInfoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CurrencyInfoEntityMapperTest {

    private final CurrencyInfoEntityMapper mapper = CurrencyInfoEntityMapper.INSTANCE;

    @Test
    void testToResponse_NormalCaseByInfo() {
        // Arrange
        CurrencyInfoEntity entity = createNormalEntity();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(entity);

        // Assert
        assertNotNull(response);
        assertEquals(entity.getId(), response.getId());
        assertEquals(entity.getDisclaimer(), response.getDisclaimer());
        assertEquals(entity.getChartName(), response.getChartName());
        assertNotNull(response.getTime());
        assertNotNull(response.getAuditTimestamps());
        assertNotNull(response.getCurrencies());
    }

    @Test
    void testToResponse_NormalCaseByTimeInfo() {
        // Arrange
        CurrencyInfoEntity entity = createNormalEntity();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(entity);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTime());
        assertEquals(entity.getTimeInfo().getUpdated(), response.getTime().getUpdated());
        assertEquals(entity.getTimeInfo().getUpdatedISO(), response.getTime().getUpdatedISO());
        assertEquals(entity.getTimeInfo().getUpdatedUK(), response.getTime().getUpdatedUK());
    }

    @Test
    void testToResponse_NormalCaseByAuditTimestamps() {
        // Arrange
        CurrencyInfoEntity entity = createNormalEntity();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(entity);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAuditTimestamps());
        assertEquals(entity.getAuditTimestamps().getUpdatedAt(), response.getAuditTimestamps().getUpdatedAt());
    }

    @ParameterizedTest
    @MethodSource("testToResponse_NormalCaseByDetailResource")
    void testToResponse_NormalCaseByCurrencies(String code) {
        // Arrange
        CurrencyInfoEntity entity = createNormalEntity();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(entity);

        // Assert
        assertNotNull(response);
        // Check currencies
        assertNotNull(response.getCurrencies());
        assertEquals(entity.getCurrencies().size(), response.getCurrencies().size());

        CurrencyDetailEntity currencyEntity = entity.getCurrencies().get(code);
        CurrencyDetailResponse currencyResponse = response.getCurrencies().get(code);
        assertNotNull(currencyResponse);
        assertEquals(currencyEntity.getCode(), currencyResponse.getCode());
        assertEquals(currencyEntity.getSymbol(), currencyResponse.getSymbol());
        assertEquals(currencyEntity.getRate(), currencyResponse.getRate());
        assertEquals(currencyEntity.getDescription(), currencyResponse.getDescription());
        assertEquals(currencyEntity.getRateFloat(), currencyResponse.getRateFloat());
    }

    private static Stream<Arguments> testToResponse_NormalCaseByDetailResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testToResponse_NullEntity() {
        // Act
        CurrencyInfoResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    void testToResponse_EmptyEntity() {
        // Arrange
        CurrencyInfoEntity entity = new CurrencyInfoEntity();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(entity);

        // Assert
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getDisclaimer());
        assertNull(response.getChartName());
        assertNull(response.getTime());
        assertNotNull(response.getAuditTimestamps()); // Default initialized in entity
        assertNotNull(response.getCurrencies()); // Default initialized in entity
        assertTrue(response.getCurrencies().isEmpty());
    }

    @Test
    void testToResponse_NullNestedObjects() {
        // Arrange
        CurrencyInfoEntity entity = new CurrencyInfoEntity();
        entity.setId(1L);
        entity.setDisclaimer("Disclaimer");
        entity.setChartName("Chart Name");
        // Don't set timeInfo to null, just don't set it at all
        entity.setAuditTimestamps(null);
        entity.setCurrencies(null);

        // Act
        CurrencyInfoResponse response = mapper.toResponse(entity);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Disclaimer", response.getDisclaimer());
        assertEquals("Chart Name", response.getChartName());
        assertNull(response.getTime());
        assertNull(response.getAuditTimestamps());
        assertNull(response.getCurrencies());
    }

    @Test
    void testToTimeInfoResponse_NormalCase() {
        // Arrange
        TimeInfoEntity entity = createTimeInfoEntity();

        // Act
        TimeInfoResponse response = mapper.toTimeInfoResponse(entity);

        // Assert
        assertNotNull(response);
        assertEquals(entity.getUpdated(), response.getUpdated());
        assertEquals(entity.getUpdatedISO(), response.getUpdatedISO());
        assertEquals(entity.getUpdatedUK(), response.getUpdatedUK());
    }

    @Test
    void testToTimeInfoResponse_NullEntity() {
        // Act
        TimeInfoResponse response = mapper.toTimeInfoResponse(null);

        // Assert
        assertNull(response);
    }

    @ParameterizedTest
    @MethodSource("testToDetailResponseMap_NormalCaseResource")
    void testToDetailResponseMap_NormalCase(String code) {
        // Arrange
        Map<String, CurrencyDetailEntity> entityMap = createCurrencyDetailEntityMap();

        // Act
        Map<String, CurrencyDetailResponse> responseMap = mapper.toDetailResponseMap(entityMap);

        // Assert
        assertNotNull(responseMap);
        assertEquals(entityMap.size(), responseMap.size());

        CurrencyDetailEntity usdEntity = entityMap.get(code);
        CurrencyDetailResponse usdResponse = responseMap.get(code);
        assertNotNull(usdResponse);
        assertEquals(usdEntity.getCode(), usdResponse.getCode());
        assertEquals(usdEntity.getSymbol(), usdResponse.getSymbol());
        assertEquals(usdEntity.getRate(), usdResponse.getRate());
        assertEquals(usdEntity.getDescription(), usdResponse.getDescription());
        assertEquals(usdEntity.getRateFloat(), usdResponse.getRateFloat());
    }

    private static Stream<Arguments> testToDetailResponseMap_NormalCaseResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testToDetailResponseMap_NullMap() {
        // Act
        Map<String, CurrencyDetailResponse> responseMap = mapper.toDetailResponseMap(null);

        // Assert
        assertNull(responseMap);
    }

    @Test
    void testToDetailResponseMap_EmptyMap() {
        // Arrange
        Map<String, CurrencyDetailEntity> entityMap = new HashMap<>();

        // Act
        Map<String, CurrencyDetailResponse> responseMap = mapper.toDetailResponseMap(entityMap);

        // Assert
        assertNotNull(responseMap);
        assertTrue(responseMap.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("testToDetailResponse_NormalCaseResource")
    void testToDetailResponse_NormalCase(CurrencyDetailEntity entity) {
        // Act
        CurrencyDetailResponse response = mapper.toDetailResponse(entity);

        // Assert
        assertNotNull(response);
        assertEquals(entity.getCode(), response.getCode());
        assertEquals(entity.getSymbol(), response.getSymbol());
        assertEquals(entity.getRate(), response.getRate());
        assertEquals(entity.getDescription(), response.getDescription());
        assertEquals(entity.getRateFloat(), response.getRateFloat());
    }

    private static Stream<Arguments> testToDetailResponse_NormalCaseResource() {
        return Stream.of(
                arguments(createCurrencyDetailEntity("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")))
        );
    }

    @Test
    void testToDetailResponse_NullEntity() {
        // Act
        CurrencyDetailResponse response = mapper.toDetailResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    void testToAuditTimestampResponse_NormalCase() {
        // Arrange
        AuditTimestamps entity = new AuditTimestamps();
        entity.setUpdatedAt(Instant.now());

        // Act
        AuditTimestampsResponse response = mapper.toAuditTimestampResponse(entity);

        // Assert
        assertNotNull(response);
        assertEquals(entity.getUpdatedAt(), response.getUpdatedAt());
    }

    @Test
    void testToAuditTimestampResponse_NullEntity() {
        // Act
        AuditTimestampsResponse response = mapper.toAuditTimestampResponse(null);

        // Assert
        assertNull(response);
    }

    // Helper methods to create test data
    private static CurrencyInfoEntity createNormalEntity() {
        CurrencyInfoEntity entity = new CurrencyInfoEntity();
        entity.setId(1L);
        entity.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD)");
        entity.setChartName("Bitcoin");

        TimeInfoEntity timeInfo = createTimeInfoEntity();
        entity.setTimeInfo(timeInfo);

        AuditTimestamps auditTimestamps = new AuditTimestamps();
        auditTimestamps.setUpdatedAt(Instant.now());
        entity.setAuditTimestamps(auditTimestamps);

        Map<String, CurrencyDetailEntity> currencies = createCurrencyDetailEntityMap();
        for (CurrencyDetailEntity currencyDetail : currencies.values()) {
            entity.putCurrency(currencyDetail);
        }

        return entity;
    }

    private static TimeInfoEntity createTimeInfoEntity() {
        TimeInfoEntity timeInfo = new TimeInfoEntity();
        timeInfo.setId(1L);
        timeInfo.setUpdated(Instant.parse("2023-01-01T12:00:00Z"));
        timeInfo.setUpdatedISO(Instant.parse("2023-01-01T12:00:00Z"));
        timeInfo.setUpdatedUK(Instant.parse("2023-01-01T12:00:00Z"));
        return timeInfo;
    }

    private static Map<String, CurrencyDetailEntity> createCurrencyDetailEntityMap() {
        Map<String, CurrencyDetailEntity> currencies = new HashMap<>();
        currencies.put("USD", createCurrencyDetailEntity("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")));
        currencies.put("EUR", createCurrencyDetailEntity("EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")));
        currencies.put("GBP", createCurrencyDetailEntity("GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.90")));
        return currencies;
    }

    private static CurrencyDetailEntity createCurrencyDetailEntity(String code, String symbol, String rate, String description, BigDecimal rateFloat) {
        CurrencyDetailEntity entity = new CurrencyDetailEntity();
        entity.setId(1L);
        entity.setCode(code);
        entity.setSymbol(symbol);
        entity.setRate(rate);
        entity.setDescription(description);
        entity.setRateFloat(rateFloat);
        return entity;
    }
}
