package com.currency.currency.service;

import com.currency.currency.entity.CurrencyRate;
import com.currency.currency.model.CurrencyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRateService currencyRateService;

    public CurrencyResponse convert(String from, String to, Double amount) {
        CurrencyRate fromRate = currencyRateService.getCurrencyRateByCode(from)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + from));
        CurrencyRate toRate = currencyRateService.getCurrencyRateByCode(to)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + to));

        double convertedAmount = (amount / fromRate.getRate()) * toRate.getRate();

        CurrencyResponse response = new CurrencyResponse();
        response.setFrom(from);
        response.setTo(to);
        response.setAmount(amount);
        response.setConvertedAmount(convertedAmount);
        return response;
    }
}