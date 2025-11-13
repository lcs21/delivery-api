package com.deliverytech.delivery_api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.deliverytech.delivery_api.projection.RelatorioVendas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.deliverytech.delivery_api.entity.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository <Restaurante, Long>{
    // Buscar por nome
    Optional<Restaurante> findByNome(String nome);

    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar por categoria
    List<Restaurante> findByCategoria(String categoria);

    // Por taxa de entrega menor ou igual
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    // Top 5 restaurantes por nome (ordem alfabética)
    List<Restaurante> findTop5ByOrderByNomeAsc();

    // No RestauranteRepository:
    @Query("SELECT r.nome as nomeRestaurante, " +
            "SUM(p.valorTotal) as totalVendas, " +
            "COUNT(p.id) as quantidePedidos " +
            "FROM Restaurante r " +
            "LEFT JOIN Pedido p ON r.id = p.restaurante.id " +
            "GROUP BY r.id, r.nome")
    List<RelatorioVendas> relatorioVendasPorRestaurante();

}