package com.currency.currency.dto;

import java.time.LocalDateTime;

public class CurrencyRateDTO {
    private String currencyCode;
    private double rate;
    private LocalDateTime lastUpdated;
    private String source;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getRate() {
        return rate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getSource() {
        return source;
    }

    // Сеттеры
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setSource(String source) {
        this.source = source;
    }
}