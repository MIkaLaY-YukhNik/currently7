package com.currency.currency.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CurrencyRateBulkDTO {
    private List<CurrencyRateDTO> currencyRates;
}

