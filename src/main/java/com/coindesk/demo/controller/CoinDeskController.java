package com.coindesk.demo.controller;

import com.coindesk.demo.dto.ResultProxy;
import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.UpdateCurrencyDetailRequest;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.dto.response.Result;
import com.coindesk.demo.service.CoinDeskService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-version}/coin_desk")
public class CoinDeskController implements ResultProxy {

    private final CoinDeskService coinDeskService;

    public CoinDeskController(CoinDeskService coinDeskService) {
        this.coinDeskService = coinDeskService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<CurrencyInfoResponse>> findAll() {
        Result<CurrencyInfoResponse> newCreated = newCreated(coinDeskService.findAll());
        return ResponseEntity.ok(newCreated);
    }

    @GetMapping(value = "/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<CurrencyDetailResponse>> findByCurrency(@PathVariable(name = "currency") String currency) {
        Result<CurrencyDetailResponse> newCreated = newCreated(coinDeskService.findByCurrency(currency));
        return ResponseEntity.ok(newCreated);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<CurrencyInfoResponse>> post(@RequestBody CurrencyInfoRequest request) {
        Result<CurrencyInfoResponse> newCreated = newCreated(coinDeskService.save(request));
        return ResponseEntity.ok(newCreated);
    }

    @PutMapping(value = "/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putByCurrency(@PathVariable(name = "currency") String currency,
                                              @RequestBody UpdateCurrencyDetailRequest request) {
        coinDeskService.updateByCurrency(currency, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable(name = "currency") String currency) {
        coinDeskService.deleteByCurrency(currency);
        return ResponseEntity.noContent().build();
    }
}
