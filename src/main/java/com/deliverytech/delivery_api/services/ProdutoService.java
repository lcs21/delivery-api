package com.deliverytech.delivery_api.services;


import com.deliverytech.delivery_api.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery_api.dto.response.ProdutoResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoService {

    ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto);

    ProdutoResponseDTO buscarPorId(Long id);

    ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto);

    ProdutoResponseDTO ativarDesativarProduto(Long id);

    ProdutoResponseDTO buscarPorNome(String nome);

    List<ProdutoResponseDTO>  buscarPorRestaurante(Long restauranteId);

    List<ProdutoResponseDTO> buscarPorCategoria(String categoria);

    List<ProdutoResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<ProdutoResponseDTO> buscarTodosProdutos();

    List<ProdutoResponseDTO> buscarPorPrecoMenorOuIgual(BigDecimal valor);
}