package com.coindesk.demo.service;

import com.coindesk.demo.dao.CurrencyInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CoinDeskService implements CoinDeskServiceProxy {

    private CurrencyInfo currencyDao;

//    public CoinDeskService(@Qualifier("httpDao") CurrencyInfo currencyDao) {
    public CoinDeskService(@Qualifier("sqlDao") CurrencyInfo currencyDao) {
        this.currencyDao = currencyDao;
    }

    public CurrencyInfo getInstance() {
        return currencyDao;
    }

}
