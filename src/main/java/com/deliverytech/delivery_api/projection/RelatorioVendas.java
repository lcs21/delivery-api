package com.deliverytech.delivery_api.projection;

import java.math.BigDecimal;

// Interface de Projeção
public interface RelatorioVendas {
    String getNomeRestauranteString();
    BigDecimal getTotalVendas();
    Long getQuantidadePedidos();
}