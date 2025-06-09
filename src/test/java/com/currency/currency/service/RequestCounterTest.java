package com.currency.currency.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestCounterTest {
    private RequestCounter requestCounter;

    @BeforeEach
    void setUp() {
        requestCounter = new RequestCounter();
    }

    @Test
    void shouldIncrementCounters() {
        requestCounter.incrementTotal();
        requestCounter.incrementSuccessful();
        requestCounter.incrementFailed();

        assertEquals(1, requestCounter.getTotalRequests());
        assertEquals(1, requestCounter.getSuccessfulRequests());
        assertEquals(1, requestCounter.getFailedRequests());
    }

    @Test
    void shouldResetAllCounters() {
        requestCounter.incrementTotal();
        requestCounter.incrementSuccessful();
        requestCounter.incrementFailed();

        requestCounter.reset();

        assertEquals(0, requestCounter.getTotalRequests());
        assertEquals(0, requestCounter.getSuccessfulRequests());
        assertEquals(0, requestCounter.getFailedRequests());
    }

    @Test
    void shouldHandleConcurrentIncrements() throws InterruptedException {
        int threads = 10;
        int iterations = 100;

        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                requestCounter.incrementTotal();
                requestCounter.incrementSuccessful();
                requestCounter.incrementFailed();
            }
        };

        Thread[] threadArray = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            threadArray[i] = new Thread(task);
            threadArray[i].start();
        }

        for (Thread thread : threadArray) {
            thread.join();
        }

        assertEquals(threads * iterations, requestCounter.getTotalRequests());
        assertEquals(threads * iterations, requestCounter.getSuccessfulRequests());
        assertEquals(threads * iterations, requestCounter.getFailedRequests());
    }
}