package com.currency.currency.service;

import com.currency.currency.entity.CurrencyRate;
import com.currency.currency.model.CurrencyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRateService currencyRateService;

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRate fromRate;

    @Mock
    private CurrencyRate toRate;

    @BeforeEach
    void setUp() {
        when(fromRate.getCurrencyCode()).thenReturn("USD");
        when(fromRate.getRate()).thenReturn(1.0);

        when(toRate.getCurrencyCode()).thenReturn("EUR");
        when(toRate.getRate()).thenReturn(0.95);
    }

    @Test
    void shouldConvertCurrencyWhenRatesExist() {
        when(currencyRateService.getCurrencyRateByCode("USD")).thenReturn(Optional.of(fromRate));
        when(currencyRateService.getCurrencyRateByCode("EUR")).thenReturn(Optional.of(toRate));
        CurrencyResponse result = currencyService.convert("USD", "EUR", 100.0);
        assertEquals("USD", result.getFrom());
        assertEquals("EUR", result.getTo());
        assertEquals(100.0, result.getAmount());
        assertEquals(95.0, result.getConvertedAmount());
        verify(currencyRateService, times(1)).getCurrencyRateByCode("USD");
        verify(currencyRateService, times(1)).getCurrencyRateByCode("EUR");
    }

    @Test
    void shouldThrowExceptionWhenFromCurrencyNotFound() {
        when(currencyRateService.getCurrencyRateByCode("USD")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> currencyService.convert("USD", "EUR", 100.0));
        verify(currencyRateService, times(1)).getCurrencyRateByCode("USD");
    }
}