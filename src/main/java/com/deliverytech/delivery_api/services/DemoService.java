package com.deliverytech.delivery_api.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    @Cacheable(cacheNames = "info", key = "'appInfo'")
    public String getInfoLenta() {
        simulateExpensiveCall();
        return "Delivery Tech API - info cache";
    }

    @CacheEvict(cacheNames = "info", key = "'appInfo'")
    public void atualizarInfo() {
    }

    private void simulateExpensiveCall() {
        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}