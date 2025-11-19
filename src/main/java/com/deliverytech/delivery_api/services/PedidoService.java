package com.deliverytech.delivery_api.services;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;


public interface PedidoService {

    PedidoResponseDTO criarPedido(PedidoRequestDTO dto);

    PedidoResponseDTO buscarPorId(Long id);

    List<PedidoResponseDTO> listarPedidosPorCliente(Long clienteId);

    PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido status);

    BigDecimal calcularValorTotalPedido(List<ItemPedidoRequestDTO> itens );

    PedidoResponseDTO cancelarPedido(Long id);

}