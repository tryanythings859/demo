package com.coindesk.demo.coindesk.dto.mapper;

import com.coindesk.demo.coindesk.dto.response.CoinDeskDetailResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskInfoResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskTimeInfoResponse;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.dto.response.TimeInfoResponse;
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

class CurrencyInfoResponseMapperTest {

    private final CurrencyInfoResponseMapper mapper = org.mapstruct.factory.Mappers.getMapper(CurrencyInfoResponseMapper.class);

    @Test
    void testToResponse_NormalCaseByInfo() {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = createNormalCoinDeskInfoResponse();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        assertEquals(coinDeskResponse.getDisclaimer(), response.getDisclaimer());
        assertEquals(coinDeskResponse.getChartName(), response.getChartName());
        assertNotNull(response.getTime());
        assertNotNull(response.getCurrencies());
    }
    @Test
    void testToResponse_NormalCaseByTimeInfo() {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = createNormalCoinDeskInfoResponse();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTime());
        assertEquals(coinDeskResponse.getTime().getUpdated(), response.getTime().getUpdated());
        assertEquals(coinDeskResponse.getTime().getUpdatedISO(), response.getTime().getUpdatedISO());
        assertEquals(coinDeskResponse.getTime().getUpdatedUK(), response.getTime().getUpdatedUK());
    }

    @ParameterizedTest
    @MethodSource("testToResponse_NormalCaseByCurrenciesResource")
    void testToResponse_NormalCaseByCurrencies(String code) {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = createNormalCoinDeskInfoResponse();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        // Check currencies
        assertNotNull(response.getCurrencies());
        assertEquals(coinDeskResponse.getCurrencies().size(), response.getCurrencies().size());

        CoinDeskDetailResponse currencyCoinDeskResponse = coinDeskResponse.getCurrencies().get(code);
        CurrencyDetailResponse currencyResponse = response.getCurrencies().get(code);
        assertNotNull(currencyResponse);
        assertEquals(currencyCoinDeskResponse.getCode(), currencyResponse.getCode());
        assertEquals(currencyCoinDeskResponse.getSymbol(), currencyResponse.getSymbol());
        assertEquals(currencyCoinDeskResponse.getRate(), currencyResponse.getRate());
        assertEquals(currencyCoinDeskResponse.getDescription(), currencyResponse.getDescription());
        assertEquals(currencyCoinDeskResponse.getRateFloat(), currencyResponse.getRateFloat());
    }

    private static Stream<Arguments> testToResponse_NormalCaseByCurrenciesResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testToResponse_NullResponse() {
        // Act
        CurrencyInfoResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    void testToResponse_EmptyResponse() {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = new CoinDeskInfoResponse();

        // Act
        CurrencyInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getDisclaimer());
        assertNull(response.getChartName());
        assertNull(response.getTime());
        assertNull(response.getAuditTimestamps());
        assertNull(response.getCurrencies());
    }

    @Test
    void testToResponse_NullNestedObjects() {
        // Arrange
        CoinDeskInfoResponse coinDeskResponse = new CoinDeskInfoResponse();
        coinDeskResponse.setDisclaimer("Disclaimer");
        coinDeskResponse.setChartName("Chart Name");
        coinDeskResponse.setTime(null);
        coinDeskResponse.setCurrencies(null);

        // Act
        CurrencyInfoResponse response = mapper.toResponse(coinDeskResponse);

        // Assert
        assertNotNull(response);
        assertNull(response.getId());
        assertEquals("Disclaimer", response.getDisclaimer());
        assertEquals("Chart Name", response.getChartName());
        assertNull(response.getTime());
        assertNull(response.getAuditTimestamps());
        assertNull(response.getCurrencies());
    }

    @Test
    void testToTimeInfoResponse_NormalCase() {
        // Arrange
        CoinDeskTimeInfoResponse coinDeskTimeResponse = createCoinDeskTimeInfoResponse();

        // Act
        TimeInfoResponse response = mapper.toTimeInfoResponse(coinDeskTimeResponse);

        // Assert
        assertNotNull(response);
        assertEquals(coinDeskTimeResponse.getUpdated(), response.getUpdated());
        assertEquals(coinDeskTimeResponse.getUpdatedISO(), response.getUpdatedISO());
        assertEquals(coinDeskTimeResponse.getUpdatedUK(), response.getUpdatedUK());
    }

    @Test
    void testToTimeInfoResponse_NullResponse() {
        // Act
        TimeInfoResponse response = mapper.toTimeInfoResponse(null);

        // Assert
        assertNull(response);
    }

    @ParameterizedTest
    @MethodSource("testToDetailResponse_NormalCaseResource")
    void testToDetailResponse_NormalCase(CoinDeskDetailResponse coinDeskDetailResponse) {
        // Act
        CurrencyDetailResponse response = mapper.toDetailResponse(coinDeskDetailResponse);

        // Assert
        assertNotNull(response);
        assertEquals(coinDeskDetailResponse.getCode(), response.getCode());
        assertEquals(coinDeskDetailResponse.getSymbol(), response.getSymbol());
        assertEquals(coinDeskDetailResponse.getRate(), response.getRate());
        assertEquals(coinDeskDetailResponse.getDescription(), response.getDescription());
        assertEquals(coinDeskDetailResponse.getRateFloat(), response.getRateFloat());
    }

    private static Stream<Arguments> testToDetailResponse_NormalCaseResource() {
        return Stream.of(
                arguments(createCoinDeskDetailResponse("USD", "$", "45,000.12", "United States Dollar", BigDecimal.valueOf(45000.12)))
        );
    }

    @Test
    void testToDetailResponse_NullResponse() {
        // Act
        CurrencyDetailResponse response = mapper.toDetailResponse(null);

        // Assert
        assertNull(response);
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
