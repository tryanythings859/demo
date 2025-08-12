package com.coindesk.demo.controller;

import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.UpdateCurrencyDetailRequest;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.service.CoinDeskService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CoinDeskControllerTest {

    // Test-specific controller subclass with hardcoded path
    @RequestMapping("/api/v1.0/coin_desk")
    static class TestCoinDeskController extends CoinDeskController {
        public TestCoinDeskController(CoinDeskService coinDeskService) {
            super(coinDeskService);
        }
    }

    @Mock
    private CoinDeskService coinDeskService;

    @InjectMocks
    private TestCoinDeskController coinDeskController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(coinDeskController)
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

    @ParameterizedTest
    @MethodSource("testFindAllByResultResource")
    void testFindAllByResult(CurrencyInfoResponse mockResponse,
                     String disclaimer,
                     String chartName
                     ) throws Exception {
        when(coinDeskService.findAll()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1.0/coin_desk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.disclaimer").value(disclaimer))
                .andExpect(jsonPath("$.result.chartName").value(chartName));

        // Verify
        verify(coinDeskService, times(1)).findAll();
    }

    private static Stream<Arguments> testFindAllByResultResource() {
        return Stream.of(
                arguments(createMockCurrencyInfoResponse(),
                        "This data was produced from the CoinDesk Bitcoin Price Index",
                        "Bitcoin"
                        )
                );
    }

    @ParameterizedTest
    @MethodSource("testFindAllByBPIResource")
    void testFindAll(CurrencyInfoResponse mockResponse,
                     String bpiCurrency,
                     String code,
                     String symbol,
                     String rate,
                     String description,
                     BigDecimal rateFloat
    ) throws Exception {
        // Arrange
        when(coinDeskService.findAll()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1.0/coin_desk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath(bpiCurrency+".code").value(code))
                .andExpect(jsonPath(bpiCurrency+".symbol").value(symbol))
                .andExpect(jsonPath(bpiCurrency+".rate").value(rate))
                .andExpect(jsonPath(bpiCurrency+".description").value(description))
                .andExpect(jsonPath(bpiCurrency+".rate_float").value(rateFloat));

        // Verify
        verify(coinDeskService, times(1)).findAll();
    }

    private static Stream<Arguments> testFindAllByBPIResource() {
        return Stream.of(
                arguments(createMockCurrencyInfoResponse(),
                        "$.result.bpi.USD",
                        "USD",
                        "$",
                        "45,000.12",
                        "United States Dollar",
                        BigDecimal.valueOf(45000.12)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testFindByCurrencyResource")
    void testFindByCurrency(CurrencyDetailResponse mockResponse,
                            String currency,
                            String symbol,
                            String rate,
                            String description,
                            BigDecimal rateFloat) throws Exception {
        when(coinDeskService.findByCurrency(eq(currency))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1.0/coin_desk/{currency}", currency)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.code").value(currency))
                .andExpect(jsonPath("$.result.symbol").value(symbol))
                .andExpect(jsonPath("$.result.rate").value(rate))
                .andExpect(jsonPath("$.result.description").value(description))
                .andExpect(jsonPath("$.result.rate_float").value(rateFloat));

        // Verify
        verify(coinDeskService, times(1)).findByCurrency(eq(currency));
    }

    private static Stream<Arguments> testFindByCurrencyResource() {
        return Stream.of(
                arguments(createCurrencyDetailResponse("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")),
                        "USD",
                        "$",
                        "45,000.12",
                        "United States Dollar",
                        BigDecimal.valueOf(45000.12)
                ),
                arguments(createCurrencyDetailResponse("EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")),
                        "EUR",
                        "€",
                        "41,234.56",
                        "Euro",
                        BigDecimal.valueOf(41234.56)
                ),
                arguments(createCurrencyDetailResponse("GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.90")),
                        "GBP",
                        "£",
                        "35,678.90",
                        "British Pound Sterling",
                        BigDecimal.valueOf(35678.90)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testPostResource")
    void testPost(CurrencyInfoRequest request,
                  CurrencyInfoResponse mockResponse,
                  String disclaimer,
                  String chartName,
                  String jsonPathByCurrency,
                  String currency) throws Exception {
        when(coinDeskService.save(any(CurrencyInfoRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1.0/coin_desk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.disclaimer").value(disclaimer))
                .andExpect(jsonPath("$.result.chartName").value(chartName))
                .andExpect(jsonPath(jsonPathByCurrency).value(currency));

        // Verify
        verify(coinDeskService, times(1)).save(any(CurrencyInfoRequest.class));
    }


    private static Stream<Arguments> testPostResource() {
        return Stream.of(
                arguments(createMockCurrencyInfoRequest(),
                        createMockCurrencyInfoResponse(),
                        "This data was produced from the CoinDesk Bitcoin Price Index",
                        "Bitcoin",
                        "$.result.bpi.USD.code",
                        "USD"
                ),
                arguments(createMockCurrencyInfoRequest(),
                        createMockCurrencyInfoResponse(),
                        "This data was produced from the CoinDesk Bitcoin Price Index",
                        "Bitcoin",
                        "$.result.bpi.EUR.code",
                        "EUR"
                ),
                arguments(createMockCurrencyInfoRequest(),
                        createMockCurrencyInfoResponse(),
                        "This data was produced from the CoinDesk Bitcoin Price Index",
                        "Bitcoin",
                        "$.result.bpi.GBP.code",
                        "GBP"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testPutByCurrencyResource")
    void testPutByCurrency(String currency, String description, String rate, BigDecimal rateFloat) throws Exception {
        UpdateCurrencyDetailRequest request = new UpdateCurrencyDetailRequest();
        request.setDescription(description);
        request.setRate(rate);
        request.setRateFloat(rateFloat);

        doNothing().when(coinDeskService).updateByCurrency(eq(currency), any(UpdateCurrencyDetailRequest.class));

        // Act & Assert
        mockMvc.perform(put("/api/v1.0/coin_desk/{currency}", currency)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        // Verify
        verify(coinDeskService, times(1)).updateByCurrency(eq(currency), any(UpdateCurrencyDetailRequest.class));
    }

    private static Stream<Arguments> testPutByCurrencyResource() {
        return Stream.of(
                arguments("USD", "Updated United States Dollar", "46,000.00", new BigDecimal("46000.00")),
                arguments("EUR", "Updated United States Dollar", "46,000.00", new BigDecimal("46000.00")),
                arguments("GBP", "Updated United States Dollar", "46,000.00", new BigDecimal("46000.00"))
        );
    }

    @ParameterizedTest
    @MethodSource("testDeleteResource")
    void testDelete(String currency) throws Exception {
        doNothing().when(coinDeskService).deleteByCurrency(eq(currency));

        // Act & Assert
        mockMvc.perform(delete("/api/v1.0/coin_desk/{currency}", currency)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

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

        Map<String, CurrencyDetailResponse> currencies = new HashMap<>();
        currencies.put("USD", createCurrencyDetailResponse("USD", "$", "45,000.12", "United States Dollar", new BigDecimal("45000.12")));
        currencies.put("EUR", createCurrencyDetailResponse("EUR", "€", "41,234.56", "Euro", new BigDecimal("41234.56")));
        currencies.put("GBP", createCurrencyDetailResponse("GBP", "£", "35,678.90", "British Pound Sterling", new BigDecimal("35678.90")));

        response.setCurrencies(currencies);
        return response;
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
