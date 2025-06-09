package com.currency.currency.service;

import com.currency.currency.dto.CurrencyRateBulkDTO;
import com.currency.currency.entity.CurrencyRate;
import com.currency.currency.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Override
    @Cacheable(value = "currencyRateCache")
    public List<CurrencyRate> getAllCurrencyRates() {
        return currencyRateRepository.findAll();
    }

    @Override
    @Cacheable(value = "currencyRateCache", key = "#currencyCode")
    public Optional<CurrencyRate> getCurrencyRateByCode(String currencyCode) {
        return currencyRateRepository.findByCurrencyCode(currencyCode);
    }

    @Override
    @CachePut(value = "currencyRateCache", key = "#currencyRate.currencyCode")
    public CurrencyRate createCurrencyRate(CurrencyRate currencyRate) {
        return currencyRateRepository.save(currencyRate);
    }

    @Override
    @Transactional
    @CacheEvict(value = "currencyRateCache", allEntries = true)
    public List<CurrencyRate> createBulkCurrencyRates(CurrencyRateBulkDTO bulkDTO) {
        List<CurrencyRate> rates = bulkDTO.getCurrencyRates().stream()
                .map(dto -> {
                    CurrencyRate rate = new CurrencyRate();
                    rate.setCurrencyCode(dto.getCurrencyCode());
                    rate.setRate(dto.getRate());
                    rate.setLastUpdated(dto.getLastUpdated());
                    rate.setSource(dto.getSource());
                    return rate;
                })
                .collect(Collectors.toList());
        return currencyRateRepository.saveAll(rates);
    }

    @Override
    @Transactional
    @CachePut(value = "currencyRateCache", key = "#currencyCode")
    public CurrencyRate updateCurrencyRate(String currencyCode, CurrencyRate currencyRateDetails) {
        CurrencyRate currencyRate = currencyRateRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new RuntimeException("CurrencyRate not found with code: " + currencyCode));
        currencyRate.setRate(currencyRateDetails.getRate());
        currencyRate.setLastUpdated(currencyRateDetails.getLastUpdated());
        currencyRate.setSource(currencyRateDetails.getSource());
        return currencyRateRepository.save(currencyRate);
    }

    @Override
    @Transactional
    @CacheEvict(value = "currencyRateCache", key = "#currencyCode")
    public void deleteCurrencyRate(String currencyCode) {
        CurrencyRate currencyRate = currencyRateRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new RuntimeException("CurrencyRate not found with code: " + currencyCode));
        currencyRateRepository.delete(currencyRate);
    }

    @Override
    public long getCurrencyRateCount() {
        return currencyRateRepository.count();
    }
}