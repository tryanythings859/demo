package com.coindesk.demo.dto.mapper;

import com.coindesk.demo.dto.request.CurrencyDetailRequest;
import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.TimeInfoRequest;
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

class CurrencyInfoRequestMapperTest {

    private final CurrencyInfoRequestMapper mapper = CurrencyInfoRequestMapper.INSTANCE;

    @Test
    void testToEntity_NormalCaseByInfo() {
        // Arrange
        CurrencyInfoRequest request = createNormalRequest();

        // Act
        CurrencyInfoEntity entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId()); // ID should be ignored in mapping
        assertNotNull(entity.getAuditTimestamps()); // Should be initialized but ignored in mapping
        assertEquals(request.getDisclaimer(), entity.getDisclaimer());
        assertEquals(request.getChartName(), entity.getChartName());
        assertNotNull(entity.getTimeInfo());
        assertNotNull(entity.getCurrencies());
    }

    @Test
    void testToEntity_NormalCaseByTimeInfo() {
        // Arrange
        CurrencyInfoRequest request = createNormalRequest();

        // Act
        CurrencyInfoEntity entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertNotNull(entity.getTimeInfo());
        assertEquals(request.getTime().getUpdated(), entity.getTimeInfo().getUpdated());
        assertEquals(request.getTime().getUpdatedISO(), entity.getTimeInfo().getUpdatedISO());
        assertEquals(request.getTime().getUpdatedUK(), entity.getTimeInfo().getUpdatedUK());

    }

    @ParameterizedTest
    @MethodSource("testToEntity_NormalCaseResource")
    void testToEntity_NormalCaseByDetail(String code) {
        // Arrange
        CurrencyInfoRequest request = createNormalRequest();

        // Act
        CurrencyInfoEntity entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertNotNull(entity.getCurrencies());
        assertEquals(request.getCurrencies().size(), entity.getCurrencies().size());
        CurrencyDetailRequest usdRequest = request.getCurrencies().get(code);
        CurrencyDetailEntity usdEntity = entity.getCurrencies().get(code);
        assertNotNull(usdEntity);
        assertEquals(usdRequest.getCode(), usdEntity.getCode());
        assertEquals(usdRequest.getSymbol(), usdEntity.getSymbol());
        assertEquals(usdRequest.getRate(), usdEntity.getRate());
        assertEquals(usdRequest.getDescription(), usdEntity.getDescription());
        assertEquals(usdRequest.getRateFloat(), usdEntity.getRateFloat());
    }

    private static Stream<Arguments> testToEntity_NormalCaseResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testToEntity_NullRequest() {
        // Act
        CurrencyInfoEntity entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    void testToEntity_EmptyRequest() {
        // Arrange
        CurrencyInfoRequest request = new CurrencyInfoRequest();
        // Add an empty TimeInfoRequest to avoid NullPointerException
        request.setTime(new TimeInfoRequest());

        // Act
        CurrencyInfoEntity entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNotNull(entity.getAuditTimestamps());
        assertNull(entity.getDisclaimer());
        assertNull(entity.getChartName());
        assertNotNull(entity.getTimeInfo()); // TimeInfo should not be null
        assertNull(entity.getTimeInfo().getUpdated()); // But its properties should be null
        assertNull(entity.getTimeInfo().getUpdatedISO());
        assertNull(entity.getTimeInfo().getUpdatedUK());
        assertNull(entity.getCurrencies());
    }

    @Test
    void testToEntity_NullNestedObjects() {
        // Arrange
        CurrencyInfoRequest request = new CurrencyInfoRequest();
        request.setDisclaimer("Disclaimer");
        request.setChartName("Chart Name");
        // Add an empty TimeInfoRequest instead of null to avoid NullPointerException
        request.setTime(new TimeInfoRequest());
        request.setCurrencies(null);

        // Act
        CurrencyInfoEntity entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNotNull(entity.getAuditTimestamps());
        assertEquals("Disclaimer", entity.getDisclaimer());
        assertEquals("Chart Name", entity.getChartName());
        assertNotNull(entity.getTimeInfo()); // TimeInfo should not be null
        assertNull(entity.getTimeInfo().getUpdated()); // But its properties should be null
        assertNull(entity.getTimeInfo().getUpdatedISO());
        assertNull(entity.getTimeInfo().getUpdatedUK());
        assertNull(entity.getCurrencies());
    }

    @Test
    void testToTimeInfoEntity_NormalCase() {
        // Arrange
        TimeInfoRequest request = createTimeInfoRequest();

        // Act
        TimeInfoEntity entity = mapper.toTimeInfoEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId()); // ID should be ignored in mapping
        assertNull(entity.getCurrencyInfo()); // CurrencyInfo should be ignored in mapping
        assertEquals(request.getUpdated(), entity.getUpdated());
        assertEquals(request.getUpdatedISO(), entity.getUpdatedISO());
        assertEquals(request.getUpdatedUK(), entity.getUpdatedUK());
    }

    @Test
    void testToTimeInfoEntity_NullRequest() {
        // Act
        TimeInfoEntity entity = mapper.toTimeInfoEntity(null);

        // Assert
        assertNull(entity);
    }

    @ParameterizedTest
    @MethodSource("testToPriceDetailEntityMap_NormalCaseResource")
    void testToPriceDetailEntityMap_NormalCase(String code) {
        // Arrange
        Map<String, CurrencyDetailRequest> requestMap = createCurrencyDetailRequestMap();

        // Act
        Map<String, CurrencyDetailEntity> entityMap = mapper.toPriceDetailEntityMap(requestMap);

        // Assert
        assertNotNull(entityMap);
        assertEquals(requestMap.size(), entityMap.size());

        CurrencyDetailRequest usdRequest = requestMap.get(code);
        CurrencyDetailEntity usdEntity = entityMap.get(code);
        assertNotNull(usdEntity);
        assertEquals(usdRequest.getCode(), usdEntity.getCode());
        assertEquals(usdRequest.getSymbol(), usdEntity.getSymbol());
        assertEquals(usdRequest.getRate(), usdEntity.getRate());
        assertEquals(usdRequest.getDescription(), usdEntity.getDescription());
        assertEquals(usdRequest.getRateFloat(), usdEntity.getRateFloat());
    }

    private static Stream<Arguments> testToPriceDetailEntityMap_NormalCaseResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testToPriceDetailEntityMap_NullMap() {
        // Act
        Map<String, CurrencyDetailEntity> entityMap = mapper.toPriceDetailEntityMap(null);

        // Assert
        assertNull(entityMap);
    }

    @Test
    void testToPriceDetailEntityMap_EmptyMap() {
        // Arrange
        Map<String, CurrencyDetailRequest> requestMap = new HashMap<>();

        // Act
        Map<String, CurrencyDetailEntity> entityMap = mapper.toPriceDetailEntityMap(requestMap);

        // Assert
        assertNotNull(entityMap);
        assertTrue(entityMap.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("testToDetailEntity_NormalCaseResource")
    void testToDetailEntity_NormalCase(CurrencyDetailRequest request) {
        // Act
        CurrencyDetailEntity entity = mapper.toDetailEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId()); // ID should be ignored in mapping
        assertNull(entity.getCurrencyInfo()); // CurrencyInfo should be ignored in mapping
        assertEquals(request.getCode(), entity.getCode());
        assertEquals(request.getSymbol(), entity.getSymbol());
        assertEquals(request.getRate(), entity.getRate());
        assertEquals(request.getDescription(), entity.getDescription());
        assertEquals(request.getRateFloat(), entity.getRateFloat());
    }

    private static Stream<Arguments> testToDetailEntity_NormalCaseResource() {
        return Stream.of(
                arguments(createCurrencyDetailRequest("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")))
        );
    }

    @Test
    void testToDetailEntity_NullRequest() {
        // Act
        CurrencyDetailEntity entity = mapper.toDetailEntity(null);

        // Assert
        assertNull(entity);
    }

    @ParameterizedTest
    @MethodSource("testLinkParentResource")
    void testLinkParent(String code) {
        // Arrange
        CurrencyInfoEntity entity = new CurrencyInfoEntity();
        Map<String, CurrencyDetailEntity> currencies = new HashMap<>();

        CurrencyDetailEntity currency = new CurrencyDetailEntity();
        currency.setCode(code);
        currencies.put(code, currency);

        entity.setCurrencies(currencies);

        // Act
        mapper.linkParent(entity);

        // Assert
        for (CurrencyDetailEntity currencyDetail : entity.getCurrencies().values()) {
            assertSame(entity, currencyDetail.getCurrencyInfo());
        }
    }

    private static Stream<Arguments> testLinkParentResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testLinkParent_NullCurrencies() {
        // Arrange
        CurrencyInfoEntity entity = new CurrencyInfoEntity();
        entity.setCurrencies(null);

        // Act & Assert (should not throw exception)
        mapper.linkParent(entity);
    }

    // Helper methods to create test data
    private static CurrencyInfoRequest createNormalRequest() {
        CurrencyInfoRequest request = new CurrencyInfoRequest();
        request.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD)");
        request.setChartName("Bitcoin");

        TimeInfoRequest timeInfo = createTimeInfoRequest();
        request.setTime(timeInfo);

        Map<String, CurrencyDetailRequest> currencies = createCurrencyDetailRequestMap();
        request.setCurrencies(currencies);

        return request;
    }

    private static TimeInfoRequest createTimeInfoRequest() {
        TimeInfoRequest timeInfo = new TimeInfoRequest();
        timeInfo.setUpdated(Instant.parse("2023-01-01T12:00:00Z"));
        timeInfo.setUpdatedISO(Instant.parse("2023-01-01T12:00:00Z"));
        timeInfo.setUpdatedUK(Instant.parse("2023-01-01T12:00:00Z"));
        return timeInfo;
    }

    private static Map<String, CurrencyDetailRequest> createCurrencyDetailRequestMap() {
        Map<String, CurrencyDetailRequest> currencies = new HashMap<>();
        currencies.put("USD", createCurrencyDetailRequest("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")));
        currencies.put("EUR", createCurrencyDetailRequest("EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")));
        currencies.put("GBP", createCurrencyDetailRequest("GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.90")));
        return currencies;
    }

    private static CurrencyDetailRequest createCurrencyDetailRequest(String code, String symbol, String rate, String description, BigDecimal rateFloat) {
        CurrencyDetailRequest request = new CurrencyDetailRequest();
        request.setCode(code);
        request.setSymbol(symbol);
        request.setRate(rate);
        request.setDescription(description);
        request.setRateFloat(rateFloat);
        return request;
    }
}
