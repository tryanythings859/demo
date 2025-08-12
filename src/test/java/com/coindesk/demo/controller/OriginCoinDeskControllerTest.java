package com.coindesk.demo.controller;

import com.coindesk.demo.coindesk.dto.response.NewCoinDeskDetailResponse;
import com.coindesk.demo.coindesk.dto.response.NewCoinDeskInfoResponse;
import com.coindesk.demo.coindesk.service.OriginCoinDeskService;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.entity.CurrencyDetailEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OriginCoinDeskControllerTest {

    // Test-specific controller subclass with hardcoded path
    @RequestMapping("/api/v1.0/origin_coin_desk")
    static class TestOriginCoinDeskController extends OriginCoinDeskController {
        public TestOriginCoinDeskController(OriginCoinDeskService originCoinDeskService) {
            super(originCoinDeskService);
        }
    }

    @Mock
    private OriginCoinDeskService originCoinDeskService;

    @InjectMocks
    private TestOriginCoinDeskController originCoinDeskController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(originCoinDeskController)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .build();
        objectMapper = new ObjectMapper();
    }

    // Mock exception handler to handle any controller exceptions
    private static class ExceptionHandlerAdvice {
        @org.springframework.web.bind.annotation.ExceptionHandler
        public org.springframework.http.ResponseEntity<String> handle(Exception e) {
            return new org.springframework.http.ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    void testFindOrigin() throws Exception {
        // Arrange
        CurrencyInfoResponse mockResponse = createMockCurrencyInfoResponse();
        when(originCoinDeskService.findAll()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1.0/origin_coin_desk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.disclaimer").value("This data was produced from the CoinDesk Bitcoin Price Index"))
                .andExpect(jsonPath("$.result.chartName").value("Bitcoin"));

        // Verify
        verify(originCoinDeskService, times(1)).findAll();
    }

    @ParameterizedTest
    @MethodSource("testFindOriginByCurrencyResource")
    void testFindOriginByCurrency(
            String bpiCurrency,
            String code,
            String symbol,
            String rate,
            String description,
            BigDecimal rateFloat
    ) throws Exception {
        // Arrange
        CurrencyInfoResponse mockResponse = createMockCurrencyInfoResponse();
        when(originCoinDeskService.findAll()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1.0/origin_coin_desk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath(bpiCurrency + ".code").value(code))
                .andExpect(jsonPath(bpiCurrency + ".symbol").value(symbol))
                .andExpect(jsonPath(bpiCurrency + ".rate").value(rate))
                .andExpect(jsonPath(bpiCurrency + ".description").value(description))
                .andExpect(jsonPath(bpiCurrency + ".rate_float").value(rateFloat));

        // Verify
        verify(originCoinDeskService, times(1)).findAll();
    }

    private static Stream<Arguments> testFindOriginByCurrencyResource() {
        return Stream.of(
                arguments("$.result.bpi.USD", "USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")),
                arguments("$.result.bpi.EUR", "EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")),
                arguments("$.result.bpi.GBP", "GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.9"))
        );
    }

    @Test
    void testNewResponse() throws Exception {
        // Arrange
        NewCoinDeskInfoResponse mockResponse = createMockNewCoinDeskInfoResponse();
        when(originCoinDeskService.newResponse()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1.0/origin_coin_desk/new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.disclaimer").value("This data was produced from the CoinDesk Bitcoin Price Index"))
                .andExpect(jsonPath("$.result.chartName").value("Bitcoin"));

        // Verify
        verify(originCoinDeskService, times(1)).newResponse();
    }

    @ParameterizedTest
    @MethodSource("testNewResponseByCurrencyResource")
    void testNewResponseByCurrency(
            String bpiCurrency,
            String code,
            String zhCode,
            String symbol,
            String rate,
            String description,
            BigDecimal rateFloat) throws Exception {
        // Arrange
        NewCoinDeskInfoResponse mockResponse = createMockNewCoinDeskInfoResponse();
        when(originCoinDeskService.newResponse()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1.0/origin_coin_desk/new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath(bpiCurrency + ".code").value(code))
                .andExpect(jsonPath(bpiCurrency + ".zhCode").value(zhCode))
                .andExpect(jsonPath(bpiCurrency + ".symbol").value(symbol))
                .andExpect(jsonPath(bpiCurrency + ".rate").value(rate))
                .andExpect(jsonPath(bpiCurrency + ".description").value(description))
                .andExpect(jsonPath(bpiCurrency + ".rate_float").value(rateFloat));

        // Verify
        verify(originCoinDeskService, times(1)).newResponse();
    }

    private static Stream<Arguments> testNewResponseByCurrencyResource() {
        return Stream.of(
                arguments("$.result.bpi.USD", "USD", "美元", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")),
                arguments("$.result.bpi.EUR", "EUR", "歐元", "€", "41,234.56", "Euro", new BigDecimal("41234.56")),
                arguments("$.result.bpi.GBP", "GBP", "英鎊", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.9"))
        );
    }

    // Helper methods to create mock data
    private static CurrencyInfoResponse createMockCurrencyInfoResponse() {
        CurrencyInfoResponse response = new CurrencyInfoResponse();
        response.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index");
        response.setChartName("Bitcoin");

        Map<String, CurrencyDetailResponse> currencies = createCurrencyDetailResponseMap();
        response.setCurrencies(currencies);
        return response;
    }

    private static Map<String, CurrencyDetailResponse> createCurrencyDetailResponseMap() {
        Map<String, CurrencyDetailResponse> currencies = new HashMap<>();
        currencies.put("USD", createCurrencyDetailResponse("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")));
        currencies.put("EUR", createCurrencyDetailResponse("EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")));
        currencies.put("GBP", createCurrencyDetailResponse("GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.90")));
        return currencies;
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

    private static NewCoinDeskInfoResponse createMockNewCoinDeskInfoResponse() {
        NewCoinDeskInfoResponse response = new NewCoinDeskInfoResponse();
        response.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index");
        response.setChartName("Bitcoin");
        response.setUpdated(Instant.now());

        Map<String, NewCoinDeskDetailResponse> currencies = createNewCoinDeskDetailResponseMap();
        response.setCurrencies(currencies);
        return response;
    }

    private static Map<String, NewCoinDeskDetailResponse> createNewCoinDeskDetailResponseMap() {
        Map<String, NewCoinDeskDetailResponse> currencies = new HashMap<>();
        currencies.put("USD", createNewCoinDeskDetailResponse("USD", "$", "45,000.12", "United States Dollar", BigDecimal.valueOf(45000.12)));
        currencies.put("EUR", createNewCoinDeskDetailResponse("EUR", "€", "41,234.56", "Euro", BigDecimal.valueOf(41234.56)));
        currencies.put("GBP", createNewCoinDeskDetailResponse("GBP", "£", "35,678.90", "British Pound Sterling", BigDecimal.valueOf(35678.90)));
        return currencies;
    }

    private static NewCoinDeskDetailResponse createNewCoinDeskDetailResponse(String code, String symbol, String rate, String description, BigDecimal rateFloat) {
        NewCoinDeskDetailResponse response = new NewCoinDeskDetailResponse();
        response.setCode(code);
        response.setZhCode(code); // Same as code for simplicity
        response.setSymbol(symbol);
        response.setRate(rate);
        response.setDescription(description);
        response.setRateFloat(rateFloat);
        return response;
    }
}
