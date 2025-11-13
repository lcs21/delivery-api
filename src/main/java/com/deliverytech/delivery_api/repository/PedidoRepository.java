package com.deliverytech.delivery_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository <Pedido, Long> {

    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

    // Pedidos por cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Pedidos por status
    List<Pedido> findByStatus(StatusPedido status);

    // 10 pedidos mais recentes
    List<Pedido> findTop10ByOrderByDataPedidoDesc();

    // Pedidos por período
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

}