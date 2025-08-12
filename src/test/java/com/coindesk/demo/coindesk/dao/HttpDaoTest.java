package com.coindesk.demo.coindesk.dao;

import com.coindesk.demo.coindesk.dto.mapper.CurrencyInfoResponseMapper;
import com.coindesk.demo.coindesk.dto.request.CoinDeskBean;
import com.coindesk.demo.coindesk.dto.response.CoinDeskDetailResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskInfoResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskTimeInfoResponse;
import com.coindesk.demo.util.AssertHasJsonPropertyHelper;
import com.coindesk.demo.util.http.HttpTemplateStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpDaoTest implements AssertHasJsonPropertyHelper {

    @Mock
    private HttpTemplateStrategy httpTemplate;

    @Mock
    private CurrencyInfoResponseMapper currencyInfoResponseMapper;

    private HttpDao httpDao;

    @BeforeEach
    void setUp() {
        httpDao = new HttpDao(httpTemplate, currencyInfoResponseMapper);
    }

    @Test
    void testFindAll_VerifyResponseStructureByTimeInfo() {
        // Arrange
        CoinDeskInfoResponse mockResponse = createMockCoinDeskInfoResponse();
        when(httpTemplate.doRequest(any(CoinDeskBean.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CoinDeskInfoResponse result = httpDao.findAll(new CoinDeskBean());

        // Assert
        assertNotNull(result);

        // Verify time structure
        CoinDeskTimeInfoResponse timeInfo = result.getTime();
        assertNotNull(timeInfo);
        assertNotNull(timeInfo.getUpdated());
        assertNotNull(timeInfo.getUpdatedISO());
        assertNotNull(timeInfo.getUpdatedUK());
    }

    @Test
    void testFindAll_VerifyResponseStructureByInfo() {
        // Arrange
        CoinDeskInfoResponse mockResponse = createMockCoinDeskInfoResponse();
        when(httpTemplate.doRequest(any(CoinDeskBean.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CoinDeskInfoResponse result = httpDao.findAll(new CoinDeskBean());

        // Assert
        assertNotNull(result);

        assertNotNull(result.getTime());
        assertNotNull(result.getDisclaimer());
        assertNotNull(result.getChartName());
        assertNotNull(result.getCurrencies());
        assertFalse(result.getCurrencies().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("testFindAll_VerifyResponseStructureByDetailKeyResource")
    void testFindAll_VerifyResponseStructureByDetailKey(String key) {
        // Arrange
        CoinDeskInfoResponse mockResponse = createMockCoinDeskInfoResponse();
        when(httpTemplate.doRequest(any(CoinDeskBean.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CoinDeskInfoResponse result = httpDao.findAll(new CoinDeskBean());

        // Assert
        assertNotNull(result);

        // Verify currencies structure
        assertNotNull(result.getCurrencies());
        assertFalse(result.getCurrencies().isEmpty());

        CoinDeskDetailResponse usdDetail = result.getCurrencies().get(key);
        assertNotNull(usdDetail);
    }

    @ParameterizedTest
    @MethodSource("testFindAll_VerifyResponseStructureByDetailKeyResource")
    void testFindAll_VerifyResponseStructureByDetailValue(String key) {
        // Arrange
        CoinDeskInfoResponse mockResponse = createMockCoinDeskInfoResponse();
        when(httpTemplate.doRequest(any(CoinDeskBean.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CoinDeskInfoResponse result = httpDao.findAll(new CoinDeskBean());

        // Assert
        assertNotNull(result);

        // Verify currencies structure
        assertNotNull(result.getCurrencies());
        assertFalse(result.getCurrencies().isEmpty());

        CoinDeskDetailResponse usdDetail = result.getCurrencies().get(key);
        assertNotNull(usdDetail);
        assertNotNull(usdDetail.getCode());
        assertNotNull(usdDetail.getSymbol());
        assertNotNull(usdDetail.getRate());
        assertNotNull(usdDetail.getDescription());
    }

    private static Stream<Arguments> testFindAll_VerifyResponseStructureByDetailKeyResource() {
        return Stream.of(
                arguments("USD"),
                arguments("EUR"),
                arguments("GBP")
        );
    }

    @Test
    void testFindAll_VerifyJsonPropertyNamesByTimeInfo() {
        // Arrange
        CoinDeskInfoResponse mockResponse = createMockCoinDeskInfoResponse();
        when(httpTemplate.doRequest(any(CoinDeskBean.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CoinDeskInfoResponse result = httpDao.findAll(new CoinDeskBean());

        // Assert
        assertNotNull(result);

        // Verify time info JSON property names
        assertHasJsonProperty(CoinDeskTimeInfoResponse.class, "updated");
        assertHasJsonProperty(CoinDeskTimeInfoResponse.class, "updatedISO");
        assertHasJsonProperty(CoinDeskTimeInfoResponse.class, "updateduk");
    }


    @Test
    void testFindAll_VerifyJsonPropertyNamesByInfo() {
        // Arrange
        CoinDeskInfoResponse mockResponse = createMockCoinDeskInfoResponse();
        when(httpTemplate.doRequest(any(CoinDeskBean.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CoinDeskInfoResponse result = httpDao.findAll(new CoinDeskBean());

        // Assert
        assertNotNull(result);

        // Verify JSON property names using reflection
        assertHasJsonProperty(CoinDeskInfoResponse.class, "time");
        assertHasJsonProperty(CoinDeskInfoResponse.class, "disclaimer");
        assertHasJsonProperty(CoinDeskInfoResponse.class, "chartName");
        assertHasJsonProperty(CoinDeskInfoResponse.class, "bpi");
    }

    @Test
    void testFindAll_VerifyJsonPropertyNamesByDetail() {
        // Arrange
        CoinDeskInfoResponse mockResponse = createMockCoinDeskInfoResponse();
        when(httpTemplate.doRequest(any(CoinDeskBean.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CoinDeskInfoResponse result = httpDao.findAll(new CoinDeskBean());

        // Assert
        assertNotNull(result);

        // Verify currency detail JSON property names
        assertHasJsonProperty(CoinDeskDetailResponse.class, "code");
        assertHasJsonProperty(CoinDeskDetailResponse.class, "symbol");
        assertHasJsonProperty(CoinDeskDetailResponse.class, "rate");
        assertHasJsonProperty(CoinDeskDetailResponse.class, "description");
        assertHasJsonProperty(CoinDeskDetailResponse.class, "rate_float");
    }

    private CoinDeskInfoResponse createMockCoinDeskInfoResponse() {
        CoinDeskInfoResponse response = new CoinDeskInfoResponse();

        // Set disclaimer and chartName
        response.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index");
        response.setChartName("Bitcoin");

        // Set time info
        CoinDeskTimeInfoResponse timeInfo = createTimeInfoResponse();
        response.setTime(timeInfo);

        Map<String, CoinDeskDetailResponse> currencies = createCurrencyDetailResponseMap();
        response.setCurrencies(currencies);

        return response;
    }

    private static CoinDeskTimeInfoResponse createTimeInfoResponse() {
        CoinDeskTimeInfoResponse timeInfo = new CoinDeskTimeInfoResponse();
        timeInfo.setUpdated(Instant.now());
        timeInfo.setUpdatedISO(Instant.now());
        timeInfo.setUpdatedUK(Instant.now());
        return timeInfo;
    }


    private static Map<String, CoinDeskDetailResponse> createCurrencyDetailResponseMap() {
        Map<String, CoinDeskDetailResponse> currencies = new HashMap<>();
        currencies.put("USD", getCoinDeskDetailResponse(
                "EUR",
                "€",
                "41,234.56",
                "Euro",
                BigDecimal.valueOf(41234.56)
        ));
        currencies.put("EUR", getCoinDeskDetailResponse(
                "USD",
                "$",
                "45,000.12",
                "United States Dollar",
                BigDecimal.valueOf(45000.12)
        ));
        currencies.put("GBP", getCoinDeskDetailResponse(
                "GBP",
                "£",
                "35,678.90",
                "British Pound Sterling",
                BigDecimal.valueOf(35678.90)
        ));
        return currencies;
    }

    private static CoinDeskDetailResponse getCoinDeskDetailResponse(String code,
                                                                    String symbol,
                                                                    String rate,
                                                                    String description,
                                                                    BigDecimal rateFloat) {
        CoinDeskDetailResponse r = new CoinDeskDetailResponse();
        r.setCode(code);
        r.setSymbol(symbol);
        r.setRate(rate);
        r.setDescription(description);
        r.setRateFloat(rateFloat);
        return r;
    }
}