package com.coindesk.demo.controller;

import com.coindesk.demo.coindesk.dto.response.NewCoinDeskInfoResponse;
import com.coindesk.demo.coindesk.service.OriginCoinDeskService;
import com.coindesk.demo.dto.ResultProxy;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.dto.response.Result;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-version}/origin_coin_desk")
public class OriginCoinDeskController implements ResultProxy {

    private final OriginCoinDeskService coinDeskService;

    public OriginCoinDeskController(OriginCoinDeskService coinDeskService) {
        this.coinDeskService = coinDeskService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<CurrencyInfoResponse>> findOrigin() {
        Result<CurrencyInfoResponse> newCreated = newCreated(coinDeskService.findAll());
        return ResponseEntity.ok(newCreated);
    }

    @GetMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<NewCoinDeskInfoResponse>> newResponse() {
        Result<NewCoinDeskInfoResponse> newCreated = newCreated(coinDeskService.newResponse());
        return ResponseEntity.ok(newCreated);
    }

}
