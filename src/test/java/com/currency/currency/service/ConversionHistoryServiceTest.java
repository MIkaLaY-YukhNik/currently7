package com.currency.currency.service;

import com.currency.currency.dto.ConversionHistoryBulkDTO;
import com.currency.currency.entity.ConversionHistory;
import com.currency.currency.entity.CurrencyRate;
import com.currency.currency.entity.User;
import com.currency.currency.repository.ConversionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversionHistoryServiceTest {

    @Mock
    private ConversionHistoryRepository conversionHistoryRepository;

    @Mock
    private UserService userService;

    @Mock
    private CurrencyRateService currencyRateService;

    @InjectMocks
    private ConversionHistoryService conversionHistoryService;

    @Mock
    private ConversionHistory history1;

    @Mock
    private User user;

    @Mock
    private CurrencyRate rate;

    @BeforeEach
    void setUp() {
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("testuser");

        when(rate.getCurrencyCode()).thenReturn("USD");
        when(rate.getRate()).thenReturn(1.0);

        when(history1.getId()).thenReturn(1L);
        when(history1.getFromCurrency()).thenReturn("USD");
        when(history1.getToCurrency()).thenReturn("EUR");
        when(history1.getAmount()).thenReturn(100.0);
        when(history1.getConvertedAmount()).thenReturn(95.0);
        when(history1.getConvertedAt()).thenReturn(LocalDateTime.now());
        when(history1.getUser()).thenReturn(user);
        when(history1.getCurrencyRates()).thenReturn(new HashSet<>(Arrays.asList(rate)));
    }

    @Test
    void shouldReturnAllConversionHistoriesWhenRequested() {
        when(conversionHistoryRepository.findAll()).thenReturn(Arrays.asList(history1));
        List<ConversionHistory> result = conversionHistoryService.getAllConversionHistories();
        assertEquals(1, result.size());
        assertTrue(result.contains(history1));
        verify(conversionHistoryRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnConversionHistoryWhenIdExists() {
        when(conversionHistoryRepository.findById(1L)).thenReturn(Optional.of(history1));
        Optional<ConversionHistory> result = conversionHistoryService.getConversionHistoryById(1L);
        assertTrue(result.isPresent());
        assertEquals(history1, result.get());
        verify(conversionHistoryRepository, times(1)).findById(1L);
    }

    @Test
    void shouldSaveAndReturnConversionHistoryWhenCreated() {
        when(conversionHistoryRepository.save(history1)).thenReturn(history1);
        ConversionHistory result = conversionHistoryService.createConversionHistory(history1);
        assertEquals(history1, result);
        verify(conversionHistoryRepository, times(1)).save(history1);
    }

    @Test
    void shouldSaveAllConversionHistoriesWhenBulkCreated() {
        ConversionHistoryBulkDTO bulkDTO = mock(ConversionHistoryBulkDTO.class);
        ConversionHistoryBulkDTO.ConversionHistoryDTO dto1 = mock(ConversionHistoryBulkDTO.ConversionHistoryDTO.class);
        when(bulkDTO.getConversionHistories()).thenReturn(Arrays.asList(dto1));
        when(dto1.getFromCurrency()).thenReturn("USD");
        when(dto1.getToCurrency()).thenReturn("EUR");
        when(dto1.getAmount()).thenReturn(100.0);
        when(dto1.getConvertedAmount()).thenReturn(95.0);
        when(dto1.getConvertedAt()).thenReturn(LocalDateTime.now());
        when(dto1.getUserId()).thenReturn(1L);
        when(dto1.getCurrencyRateCodes()).thenReturn(new HashSet<>(Arrays.asList("USD")));

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(currencyRateService.getCurrencyRateByCode("USD")).thenReturn(Optional.of(rate));
        when(conversionHistoryRepository.saveAll(anyList())).thenReturn(Arrays.asList(history1));

        List<ConversionHistory> result = conversionHistoryService.createBulkConversionHistories(bulkDTO);
        assertEquals(1, result.size());
        assertTrue(result.contains(history1));
        verify(userService, times(1)).getUserById(1L);
        verify(currencyRateService, times(1)).getCurrencyRateByCode("USD");
        verify(conversionHistoryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldUpdateAndReturnConversionHistoryWhenIdExists() {
        ConversionHistory updatedHistory = mock(ConversionHistory.class);
        when(updatedHistory.getId()).thenReturn(1L);
        when(updatedHistory.getFromCurrency()).thenReturn("EUR");
        when(updatedHistory.getToCurrency()).thenReturn("USD");
        when(conversionHistoryRepository.findById(1L)).thenReturn(Optional.of(history1));
        when(conversionHistoryRepository.save(history1)).thenReturn(history1);
        when(history1.getFromCurrency()).thenReturn("EUR");
        ConversionHistory result = conversionHistoryService.updateConversionHistory(1L, updatedHistory);
        assertEquals("EUR", result.getFromCurrency());
        verify(conversionHistoryRepository, times(1)).findById(1L);
        verify(conversionHistoryRepository, times(1)).save(history1);
    }

    @Test
    void shouldDeleteConversionHistoryWhenIdExists() {
        when(conversionHistoryRepository.findById(1L)).thenReturn(Optional.of(history1));
        conversionHistoryService.deleteConversionHistory(1L);
        verify(conversionHistoryRepository, times(1)).findById(1L);
        verify(conversionHistoryRepository, times(1)).delete(history1);
    }
}