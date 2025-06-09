package com.currency.currency.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "rate")
    private double rate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "source")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
