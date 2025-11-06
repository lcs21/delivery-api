package com.delivery_api.Projeto.Delivery.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository <Produto, Long> {

    // buscar produto por restaurante ID
    List <Produto> findByRestauranteId(Long restauranteId);

    // Buscar produtos disponíveis
    List<Produto> findByDisponivelTrue();

    // Buscar produtos por categoria
    List<Produto> findByCategoria(String categoria);

    //Buscar produtos com preço menor ou igual ao informado
    List<Produto> findByPrecoLessThanEqual(Boolean preco);
}