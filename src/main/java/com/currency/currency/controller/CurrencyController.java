package com.currency.currency.controller;

import com.currency.currency.dto.ConvertRequest;
import com.currency.currency.model.CurrencyResponse;
import com.currency.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/convert")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public CurrencyResponse convertCurrency(ConvertRequest request) {
        return currencyService.convert(request.getFrom(), request.getTo(), request.getAmount());
    }
}