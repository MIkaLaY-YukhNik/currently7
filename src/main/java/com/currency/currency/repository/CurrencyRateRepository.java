package com.currency.currency.repository;

import com.currency.currency.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findByCurrencyCode(String currencyCode);
}