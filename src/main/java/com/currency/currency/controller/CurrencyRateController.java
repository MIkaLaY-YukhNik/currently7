package com.currency.currency.controller;

import com.currency.currency.dto.CurrencyRateBulkDTO;
import com.currency.currency.entity.CurrencyRate;
import com.currency.currency.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency-rates")
public class CurrencyRateController {

    @Autowired
    private CurrencyRateService currencyRateService;

    @GetMapping
    public List<CurrencyRate> getAllCurrencyRates() {
        return currencyRateService.getAllCurrencyRates();
    }

    @GetMapping("/{currencyCode}")
    public ResponseEntity<CurrencyRate> getCurrencyRateByCode(@PathVariable String currencyCode) {
        return currencyRateService.getCurrencyRateByCode(currencyCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CurrencyRate> createCurrencyRate(@RequestBody CurrencyRate currencyRate) {
        try {
            CurrencyRate createdRate = currencyRateService.createCurrencyRate(currencyRate);
            return ResponseEntity.ok(createdRate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CurrencyRate>> createBulkCurrencyRates(@RequestBody CurrencyRateBulkDTO bulkDTO) {
        try {
            List<CurrencyRate> createdRates = currencyRateService.createBulkCurrencyRates(bulkDTO);
            return ResponseEntity.ok(createdRates);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{currencyCode}")
    public ResponseEntity<CurrencyRate> updateCurrencyRate(@PathVariable String currencyCode, @RequestBody CurrencyRate currencyRateDetails) {
        try {
            CurrencyRate updatedRate = currencyRateService.updateCurrencyRate(currencyCode, currencyRateDetails);
            return ResponseEntity.ok(updatedRate);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{currencyCode}")
    public ResponseEntity<Void> deleteCurrencyRate(@PathVariable String currencyCode) {
        try {
            currencyRateService.deleteCurrencyRate(currencyCode);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/counter")
    public ResponseEntity<Long> getCurrencyRateCounter() {
        try {
            long count = currencyRateService.getCurrencyRateCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}