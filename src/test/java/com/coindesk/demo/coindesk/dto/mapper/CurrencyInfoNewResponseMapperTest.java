package com.coindesk.demo.coindesk.dto.mapper;

import com.coindesk.demo.coindesk.dto.response.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CurrencyInfoNewResponseMapperTest {

    private final CurrencyInfoNewResponseMapper mapper = Mappers.getMapper(CurrencyInfoNewResponseMapper.class);

    @Test
    void testToResponse_NormalCaseByInfo() {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = createNormalCoinDeskInfoResponse();

        // Act
        NewCoinDeskInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        assertEquals(coinDeskResponse.getDisclaimer(), response.getDisclaimer());
        assertEquals(coinDeskResponse.getChartName(), response.getChartName());
        assertEquals(coinDeskResponse.getTime().getUpdated(), response.getUpdated());

        // Check currencies
        assertNotNull(response.getCurrencies());
        assertEquals(coinDeskResponse.getCurrencies().size(), response.getCurrencies().size());
    }

    @ParameterizedTest
    @MethodSource("testToResponse_NormalCaseByDetailResource")
    void testToResponse_NormalCaseByDetail(String code) {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = createNormalCoinDeskInfoResponse();

        // Act
        NewCoinDeskInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);

        // Check currencies
        assertNotNull(response.getCurrencies());
        assertEquals(coinDeskResponse.getCurrencies().size(), response.getCurrencies().size());

        CoinDeskDetailResponse currencyCoinDeskResponse = coinDeskResponse.getCurrencies().get(code);
        NewCoinDeskDetailResponse currencyResponse = response.getCurrencies().get(code);
        assertNotNull(currencyResponse);
        assertEquals(currencyCoinDeskResponse.getCode(), currencyResponse.getCode());
        assertEquals(currencyCoinDeskResponse.getCode(), currencyResponse.getZhCode()); // zhCode should be same as code
        assertEquals(currencyCoinDeskResponse.getSymbol(), currencyResponse.getSymbol());
        assertEquals(currencyCoinDeskResponse.getRate(), currencyResponse.getRate());
        assertEquals(currencyCoinDeskResponse.getDescription(), currencyResponse.getDescription());
        assertEquals(currencyCoinDeskResponse.getRateFloat(), currencyResponse.getRateFloat());
    }

    private static Stream<Arguments> testToResponse_NormalCaseByDetailResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testToResponse_NullResponse() {
        // Act
        NewCoinDeskInfoResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    void testToResponse_EmptyResponse() {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = new CoinDeskInfoResponse();

        // Act
        NewCoinDeskInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        assertNull(response.getDisclaimer());
        assertNull(response.getChartName());
        assertNull(response.getUpdated());
        assertNull(response.getCurrencies());
    }

    @ParameterizedTest
    @MethodSource("testToResponse_NullNestedObjectsResource")
    void testToResponse_NullNestedObjects(String disclaimer, String chartName, CoinDeskTimeInfoResponse updated, Map<String, CoinDeskDetailResponse> currencies) {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = new CoinDeskInfoResponse();
        coinDeskResponse.setDisclaimer(disclaimer);
        coinDeskResponse.setChartName(chartName);
        coinDeskResponse.setTime(updated);
        coinDeskResponse.setCurrencies(currencies);

        // Act
        NewCoinDeskInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        assertEquals(disclaimer, response.getDisclaimer());
        assertEquals(chartName, response.getChartName());
        assertEquals(updated, response.getUpdated());
        assertEquals(currencies, response.getCurrencies());
    }

    private static Stream<Arguments> testToResponse_NullNestedObjectsResource() {
        return Stream.of(
                arguments("Disclaimer", "Chart Name", null, null),
                arguments("Disclaimer", null, null, null),
                arguments(null, "Chart Name", null, null)
        );
    }

    @Test
    void testToDetailResponse_NullResponse() {
        // Act
        NewCoinDeskDetailResponse response = mapper.toDetailResponse(null);

        // Assert
        assertNull(response);
    }

    @ParameterizedTest
    @MethodSource("testToDetailResponseResource")
    void testToDetailResponse_ExtremeValues(CoinDeskDetailResponse coinDeskDetailResponse) {
        // Act
        NewCoinDeskDetailResponse response = mapper.toDetailResponse(coinDeskDetailResponse);

        // Assert
        assertNotNull(response);
        assertEquals(coinDeskDetailResponse.getCode(), response.getCode());
        assertEquals(coinDeskDetailResponse.getCode(), response.getZhCode());
        assertEquals(coinDeskDetailResponse.getSymbol(), response.getSymbol());
        assertEquals(coinDeskDetailResponse.getRate(), response.getRate());
        assertEquals(coinDeskDetailResponse.getDescription(), response.getDescription());
        assertEquals(coinDeskDetailResponse.getRateFloat(), response.getRateFloat());
    }

    private static Stream<Arguments> testToDetailResponseResource() {
        return Stream.of(
                arguments(createCoinDeskDetailResponse("MAX", "∞", "9999999999999999999999999999", "Maximum Value Test", BigDecimal.valueOf(0.0001))),
                arguments(createCoinDeskDetailResponse("XYZ", "¥", "特殊字符测试", "Special Characters Test 特殊字符测试", BigDecimal.valueOf(9999999.99))),
                arguments(createCoinDeskDetailResponse("USD", "$", "45,000.12", "United States Dollar", BigDecimal.valueOf(45000.12)))
        );
    }

    // Helper methods to create test data
    private CoinDeskInfoResponse createNormalCoinDeskInfoResponse() {
        CoinDeskInfoResponse response = new CoinDeskInfoResponse();
        response.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD)");
        response.setChartName("Bitcoin");

        CoinDeskTimeInfoResponse timeInfo = createCoinDeskTimeInfoResponse();
        response.setTime(timeInfo);

        Map<String, CoinDeskDetailResponse> currencies = createCoinDeskDetailResponseMap();
        response.setCurrencies(currencies);

        return response;
    }

    private static CoinDeskTimeInfoResponse createCoinDeskTimeInfoResponse() {
        CoinDeskTimeInfoResponse timeInfo = new CoinDeskTimeInfoResponse();
        timeInfo.setUpdated(Instant.parse("2023-01-01T12:00:00Z"));
        timeInfo.setUpdatedISO(Instant.parse("2023-01-01T12:00:00Z"));
        timeInfo.setUpdatedUK(Instant.parse("2023-01-01T12:00:00Z"));
        return timeInfo;
    }

    private static Map<String, CoinDeskDetailResponse> createCoinDeskDetailResponseMap() {
        Map<String, CoinDeskDetailResponse> currencies = new HashMap<>();
        currencies.put("USD", createCoinDeskDetailResponse("USD", "$", "45,000.12", "United States Dollar", BigDecimal.valueOf(45000.12)));
        currencies.put("EUR", createCoinDeskDetailResponse("EUR", "€", "41,234.56", "Euro", BigDecimal.valueOf(41234.56)));
        currencies.put("GBP", createCoinDeskDetailResponse("GBP", "£", "35,678.90", "British Pound Sterling", BigDecimal.valueOf(35678.90)));
        return currencies;
    }

    private static CoinDeskDetailResponse createCoinDeskDetailResponse(String code, String symbol, String rate, String description, BigDecimal rateFloat) {
        CoinDeskDetailResponse response = new CoinDeskDetailResponse();
        response.setCode(code);
        response.setSymbol(symbol);
        response.setRate(rate);
        response.setDescription(description);
        response.setRateFloat(rateFloat);
        return response;
    }
}