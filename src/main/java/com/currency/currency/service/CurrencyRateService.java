package com.currency.currency.service;

import com.currency.currency.dto.CurrencyRateBulkDTO;
import com.currency.currency.entity.CurrencyRate;
import java.util.List;
import java.util.Optional;

public interface CurrencyRateService {
    List<CurrencyRate> getAllCurrencyRates();
    Optional<CurrencyRate> getCurrencyRateByCode(String currencyCode);
    CurrencyRate createCurrencyRate(CurrencyRate currencyRate);
    List<CurrencyRate> createBulkCurrencyRates(CurrencyRateBulkDTO bulkDTO);
    CurrencyRate updateCurrencyRate(String currencyCode, CurrencyRate currencyRateDetails);
    void deleteCurrencyRate(String currencyCode);
    long getCurrencyRateCount();
}