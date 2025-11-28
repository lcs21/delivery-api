package com.deliverytech.delivery_api.cache;

import com.deliverytech.delivery_api.services.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CacheIntegrationTest {

    @Autowired
    DemoService demoService;

    @Test
    void segundaChamadaDeveSerMaisRapidaComCache() {
        long t1 = measure(() -> demoService.getInfoLenta());
        long t2 = measure(() -> demoService.getInfoLenta());
        assertThat(t2).isLessThan(t1 / 3);
    }

    @Test
    void deveInvalidarCacheAposAtualizacao() {
        demoService.getInfoLenta();
        demoService.atualizarInfo();
        long tAfterEvict = measure(() -> demoService.getInfoLenta());
        assertThat(tAfterEvict).isGreaterThanOrEqualTo(250L);
    }

    private long measure(Runnable r) {
        long start = System.nanoTime();
        r.run();
        return (System.nanoTime() - start) / 1_000_000;
    }
}