package com.coindesk.demo.service;

import com.coindesk.demo.dao.CurrencyInfo;
import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.UpdateCurrencyDetailRequest;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;

public interface CoinDeskServiceProxy {
    CurrencyInfo getInstance();

    default CurrencyInfoResponse findAll() {
        return getInstance().findAll();
    }

    default CurrencyDetailResponse findByCurrency(String currency) {
        return getInstance().findByCurrency(currency);
    }

    default CurrencyInfoResponse save(CurrencyInfoRequest request) {
        return getInstance().save(request);
    }

    default void updateByCurrency(String currency, UpdateCurrencyDetailRequest currencyInfo) {
        getInstance().updateByCurrency(currency, currencyInfo);
    }

    default void deleteByCurrency(String currency) {
        getInstance().deleteByCurrency(currency);
    }
}
