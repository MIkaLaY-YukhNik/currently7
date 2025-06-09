package com.currency.currency.service;

import com.currency.currency.dto.CurrencyRateBulkDTO;
import com.currency.currency.entity.CurrencyRate;
import com.currency.currency.repository.CurrencyRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyRateServiceTest {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    private CurrencyRateService currencyRateService;

    @Mock
    private CurrencyRate rate1;

    @Mock
    private CurrencyRate rate2;

    @BeforeEach
    void setUp() {
        when(rate1.getCurrencyCode()).thenReturn("USD");
        when(rate1.getRate()).thenReturn(1.0);
        when(rate1.getLastUpdated()).thenReturn(LocalDateTime.now());
        when(rate1.getSource()).thenReturn("OpenExchangeRates");

        when(rate2.getCurrencyCode()).thenReturn("EUR");
        when(rate2.getRate()).thenReturn(0.95);
        when(rate2.getLastUpdated()).thenReturn(LocalDateTime.now());
        when(rate2.getSource()).thenReturn("OpenExchangeRates");
    }

    @Test
    void shouldReturnAllCurrencyRatesWhenRequested() {
        when(currencyRateRepository.findAll()).thenReturn(Arrays.asList(rate1, rate2));
        List<CurrencyRate> result = currencyRateService.getAllCurrencyRates();
        assertEquals(2, result.size());
        assertTrue(result.contains(rate1));
        assertTrue(result.contains(rate2));
        verify(currencyRateRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnCurrencyRateWhenCodeExists() {
        when(currencyRateRepository.findById("USD")).thenReturn(Optional.of(rate1));
        Optional<CurrencyRate> result = currencyRateService.getCurrencyRateByCode("USD");
        assertTrue(result.isPresent());
        assertEquals(rate1, result.get());
        verify(currencyRateRepository, times(1)).findById("USD");
    }

    @Test
    void shouldSaveAndReturnCurrencyRateWhenCreated() {
        when(currencyRateRepository.save(rate1)).thenReturn(rate1);
        CurrencyRate result = currencyRateService.createCurrencyRate(rate1);
        assertEquals(rate1, result);
        verify(currencyRateRepository, times(1)).save(rate1);
    }

    @Test
    void shouldSaveAllCurrencyRatesWhenBulkCreated() {
        CurrencyRateBulkDTO bulkDTO = mock(CurrencyRateBulkDTO.class);
        CurrencyRateBulkDTO.CurrencyRateDTO dto1 = mock(CurrencyRateBulkDTO.CurrencyRateDTO.class);
        CurrencyRateBulkDTO.CurrencyRateDTO dto2 = mock(CurrencyRateBulkDTO.CurrencyRateDTO.class);
        when(bulkDTO.getCurrencyRates()).thenReturn(Arrays.asList(dto1, dto2));
        when(dto1.getCurrencyCode()).thenReturn("USD");
        when(dto1.getRate()).thenReturn(1.0);
        when(dto1.getLastUpdated()).thenReturn(LocalDateTime.now());
        when(dto1.getSource()).thenReturn("OpenExchangeRates");
        when(dto2.getCurrencyCode()).thenReturn("EUR");
        when(dto2.getRate()).thenReturn(0.95);
        when(dto2.getLastUpdated()).thenReturn(LocalDateTime.now());
        when(dto2.getSource()).thenReturn("OpenExchangeRates");
        when(currencyRateRepository.saveAll(anyList())).thenReturn(Arrays.asList(rate1, rate2));
        List<CurrencyRate> result = currencyRateService.createBulkCurrencyRates(bulkDTO);
        assertEquals(2, result.size());
        assertTrue(result.contains(rate1));
        assertTrue(result.contains(rate2));
        verify(currencyRateRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldUpdateAndReturnCurrencyRateWhenCodeExists() {
        CurrencyRate updatedRate = mock(CurrencyRate.class);
        when(updatedRate.getCurrencyCode()).thenReturn("USD");
        when(updatedRate.getRate()).thenReturn(1.05);
        when(currencyRateRepository.findById(Long.valueOf("USD"))).thenReturn(Optional.of(rate1));
        when(currencyRateRepository.save(rate1)).thenReturn(rate1);
        when(rate1.getRate()).thenReturn(1.05);
        CurrencyRate result = currencyRateService.updateCurrencyRate("USD", updatedRate);
        assertEquals(1.05, result.getRate());
        verify(currencyRateRepository, times(1)).findById(Long.valueOf("USD"));
        verify(currencyRateRepository, times(1)).save(rate1);
    }

    @Test
    void shouldDeleteCurrencyRateWhenCodeExists() {
        when(currencyRateRepository.findById(Long.valueOf("USD"))).thenReturn(Optional.of(rate1));
        currencyRateService.deleteCurrencyRate("USD");
        verify(currencyRateRepository, times(1)).findById(Long.valueOf("USD"));
        verify(currencyRateRepository, times(1)).delete(rate1);
    }
}