package com.coindesk.demo.coindesk.dao;

import com.coindesk.demo.coindesk.dto.request.CoinDeskBean;
import com.coindesk.demo.coindesk.dto.response.CoinDeskDetailResponse;
import com.coindesk.demo.coindesk.dto.response.CoinDeskInfoResponse;
import com.coindesk.demo.coindesk.dto.mapper.CurrencyInfoResponseMapper;
import com.coindesk.demo.dao.CurrencyInfo;
import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.UpdateCurrencyDetailRequest;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.util.http.HttpTemplateStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("httpDao")
public class HttpDao implements CurrencyInfo {

    private final HttpTemplateStrategy httpTemplate;
    private final CurrencyInfoResponseMapper currencyInfoResponseMapper;

    public HttpDao(HttpTemplateStrategy httpTemplate,
                   CurrencyInfoResponseMapper currencyInfoResponseMapper) {
        this.httpTemplate = httpTemplate;
        this.currencyInfoResponseMapper = currencyInfoResponseMapper;
    }

    @Override
    public CurrencyInfoResponse findAll() {
        return currencyInfoResponseMapper.toResponse(findAll(new CoinDeskBean()));
    }

    public CoinDeskInfoResponse findAll(CoinDeskBean bean) {
        ResponseEntity<CoinDeskInfoResponse> response = httpTemplate.doRequest(bean);
        return response.getBody();
    }

    @Override
    public CurrencyDetailResponse findByCurrency(String currency) {
        return currencyInfoResponseMapper.toDetailResponse(findCoinDeskDetailByCurrency(currency));
    }

    public CoinDeskDetailResponse findCoinDeskDetailByCurrency(String currency) {
        CoinDeskInfoResponse response = findAll(new CoinDeskBean());
        return response.getDetailByCurrency(currency);
    }

    @Override
    public CurrencyInfoResponse save(CurrencyInfoRequest request) {
        throw new UnsupportedOperationException("Saving currency information is not supported yet.");
    }

    @Override
    public void updateByCurrency(String currency, UpdateCurrencyDetailRequest request) {
        throw new UnsupportedOperationException("Updating currency information is not supported yet.");
    }

    @Override
    public void deleteByCurrency(String currency) {
        throw new UnsupportedOperationException("Delete currency information is not supported yet.");
    }

}
