package com.coindesk.demo.coindesk.service;

import com.coindesk.demo.coindesk.dao.HttpDao;
import com.coindesk.demo.coindesk.dto.request.CoinDeskBean;
import com.coindesk.demo.coindesk.dto.mapper.CurrencyInfoNewResponseMapper;
import com.coindesk.demo.coindesk.dto.response.NewCoinDeskInfoResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OriginCoinDeskService {

    private final HttpDao currencyDao;
    private final CurrencyInfoNewResponseMapper currencyInfoNewResponseMapper;

    public OriginCoinDeskService(@Qualifier("httpDao") HttpDao currencyDao,
                                 CurrencyInfoNewResponseMapper currencyInfoNewResponseMapper) {
        this.currencyDao = currencyDao;
        this.currencyInfoNewResponseMapper = currencyInfoNewResponseMapper;
    }

    public CurrencyInfoResponse findAll() {
        return currencyDao.findAll();
    }

    public NewCoinDeskInfoResponse newResponse() {
        return currencyInfoNewResponseMapper.toResponse(currencyDao.findAll(new CoinDeskBean()));
    }

}
