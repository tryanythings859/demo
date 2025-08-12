package com.coindesk.demo.controller;

import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.UpdateCurrencyDetailRequest;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.dto.response.Result;
import com.coindesk.demo.service.CoinDeskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinDeskControllerSimpleTest {

    @Mock
    private CoinDeskService coinDeskService;

    @InjectMocks
    private CoinDeskController coinDeskController;

    @Test
    void testFindAll() {
        // Arrange
        CurrencyInfoResponse mockResponse = createMockCurrencyInfoResponse();
        when(coinDeskService.findAll()).thenReturn(mockResponse);

        // Act
        ResponseEntity<Result<CurrencyInfoResponse>> responseEntity = coinDeskController.findAll();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(true, responseEntity.getBody().isSuccess());
        assertEquals(mockResponse, responseEntity.getBody().getResult());

        // Verify
        verify(coinDeskService, times(1)).findAll();
    }

    @ParameterizedTest
    @MethodSource("testFindByCurrencyResource")
    void testFindByCurrency(CurrencyDetailResponse mockResponse,
                            String currency) {
        when(coinDeskService.findByCurrency(eq(currency))).thenReturn(mockResponse);

        // Act
        ResponseEntity<Result<CurrencyDetailResponse>> responseEntity = coinDeskController.findByCurrency(currency);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(true, responseEntity.getBody().isSuccess());
        assertEquals(mockResponse, responseEntity.getBody().getResult());

        // Verify
        verify(coinDeskService, times(1)).findByCurrency(eq(currency));
    }

    private static Stream<Arguments> testFindByCurrencyResource() {
        return Stream.of(
                arguments(createCurrencyDetailResponse("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")), "USD"),
                arguments(createCurrencyDetailResponse("EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")), "EUR"),
                arguments(createCurrencyDetailResponse("GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.90")), "GBP")
        );
    }

    @Test
    void testPost() {
        // Arrange
        CurrencyInfoRequest request = createMockCurrencyInfoRequest();
        CurrencyInfoResponse mockResponse = createMockCurrencyInfoResponse();
        when(coinDeskService.save(any(CurrencyInfoRequest.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<Result<CurrencyInfoResponse>> responseEntity = coinDeskController.post(request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(true, responseEntity.getBody().isSuccess());
        assertEquals(mockResponse, responseEntity.getBody().getResult());

        // Verify
        verify(coinDeskService, times(1)).save(any(CurrencyInfoRequest.class));
    }

    @ParameterizedTest
    @MethodSource("testPutByCurrencyResource")
    void testPutByCurrency(String currency) {
        // Arrange
        UpdateCurrencyDetailRequest request = new UpdateCurrencyDetailRequest();
        request.setDescription("Updated United States Dollar");
        request.setRate("46,000.00");
        request.setRateFloat(new BigDecimal("46000.00"));

        doNothing().when(coinDeskService).updateByCurrency(eq(currency), any(UpdateCurrencyDetailRequest.class));

        // Act
        ResponseEntity<Void> responseEntity = coinDeskController.putByCurrency(currency, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        // Verify
        verify(coinDeskService, times(1)).updateByCurrency(eq(currency), any(UpdateCurrencyDetailRequest.class));
    }

    private static Stream<Arguments> testPutByCurrencyResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @ParameterizedTest
    @MethodSource("testDeleteResource")
    void testDelete(String currency) {
        // Arrange
        doNothing().when(coinDeskService).deleteByCurrency(eq(currency));

        // Act
        ResponseEntity<Void> responseEntity = coinDeskController.delete(currency);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        // Verify
        verify(coinDeskService, times(1)).deleteByCurrency(eq(currency));
    }

    private static Stream<Arguments> testDeleteResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    // Helper methods to create mock data
    private static CurrencyInfoResponse createMockCurrencyInfoResponse() {
        CurrencyInfoResponse response = new CurrencyInfoResponse();
        response.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index");
        response.setChartName("Bitcoin");

        Map<String, CurrencyDetailResponse> currencies = createCurrencyDetailEntityMap();
        response.setCurrencies(currencies);
        return response;
    }

    private static Map<String, CurrencyDetailResponse> createCurrencyDetailEntityMap() {
        Map<String, CurrencyDetailResponse> currencies = new HashMap<>();
        currencies.put("USD", createCurrencyDetailResponse("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")));
        currencies.put("EUR", createCurrencyDetailResponse("EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")));
        currencies.put("GBP", createCurrencyDetailResponse("GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.90")));
        return currencies;
    }

    private static CurrencyDetailResponse createMockCurrencyDetailResponse() {
        return createCurrencyDetailResponse("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12"));
    }

    private static CurrencyDetailResponse createCurrencyDetailResponse(String code, String symbol, String rate, String description, BigDecimal rateFloat) {
        CurrencyDetailResponse response = new CurrencyDetailResponse();
        response.setCode(code);
        response.setSymbol(symbol);
        response.setRate(rate);
        response.setDescription(description);
        response.setRateFloat(rateFloat);
        return response;
    }

    private static CurrencyInfoRequest createMockCurrencyInfoRequest() {
        CurrencyInfoRequest request = new CurrencyInfoRequest();
        request.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index");
        request.setChartName("Bitcoin");
        return request;
    }
}
