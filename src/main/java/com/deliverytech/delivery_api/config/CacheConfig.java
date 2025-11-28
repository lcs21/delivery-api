package com.deliverytech.delivery_api.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache local simples com ConcurrentMapCache.
     * Quando SPRING_CACHE_TYPE=redis estiver definido (Docker/Compose),
     * o autoconfig do Spring Data Redis cria e usa RedisCacheManager automaticamente.
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("clientes", "pedidos", "produtos", "info");
    }
}