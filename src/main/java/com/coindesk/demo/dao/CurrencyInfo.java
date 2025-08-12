package com.coindesk.demo.dao;

import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.UpdateCurrencyDetailRequest;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;

public interface CurrencyInfo {
    CurrencyInfoResponse findAll();

    CurrencyDetailResponse findByCurrency(String currency);

    CurrencyInfoResponse save(CurrencyInfoRequest request);

    void updateByCurrency(String currency, UpdateCurrencyDetailRequest currencyInfo);

    void deleteByCurrency(String currency);
}
