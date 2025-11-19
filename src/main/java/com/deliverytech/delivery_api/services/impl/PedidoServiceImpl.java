package com.deliverytech.delivery_api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.services.PedidoService;


@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        // 1. Validar cliente existe e está ativo
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        if (!cliente.isAtivo()) {
            throw new BusinessException("Cliente inativo não pode fazer pedidos");
        }

        // 2. Validar restaurante existe e está ativo
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new BusinessException("Restaurante não encontrado"));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Restaurante não está disponível");
        }

        // 3. Validar todos os produtos existem e estão disponíveis
        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDTO : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + itemDTO.getProdutoId()));

            if (!produto.isAtivo()) {
                throw new BusinessException("Produto indisponível: " + produto.getNome());
            }

            if (!produto.getRestaurante().getId().equals(dto.getRestauranteId())) {
                throw new BusinessException("Produto não pertence ao restaurante selecionado");
            }

            // Criar item do pedido
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            itensPedido.add(item);
            subtotal = subtotal.add(item.getSubtotal());
        }

        // 4. Calcular total do pedido
        BigDecimal taxaEntrega = restaurante.getTaxaEntrega();
        BigDecimal valorTotal = subtotal.add(taxaEntrega);

        // 5. Salvar pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setObservacoes(dto.getObservacoes());
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE.name());
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setTaxaEntrega(taxaEntrega);
        pedido.setValorTotal(valorTotal);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // 6. Salvar itens do pedido
        for (ItemPedido item : itensPedido) {
            item.setPedido(pedidoSalvo);
        }
        pedidoSalvo.setItens(itensPedido);

        // 7. Atualizar estoque (se aplicável) - Simulação
        // Em um cenário real, aqui seria decrementado o estoque

        // 8. Retornar pedido criado
        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO buscarPorId(Long id) {
        // Buscar pedido por ID
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        // Converter entidade para DTO
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public List<PedidoResponseDTO> listarPedidosPorCliente(Long clienteId) {
        // Buscar pedidos por cliente ID
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        if (pedidos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pedido encontrado para o cliente com ID: " + clienteId);
        }
        // Converter lista de entidades para lista de DTOs
        return pedidos.stream()
                .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
                .toList();
    }

    @Override
    public PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido status) {
        // Buscar pedido por ID
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        // Atualizar status do pedido
        isTransicaoValida(status, status);
        if (!isTransicaoValida(StatusPedido.valueOf(pedido.getStatus()), status)) {
            throw new BusinessException("Transição de status inválida para o pedido com ID: " + id);
        }
        pedido.setStatus(status.name());
        // Salvar pedido atualizado
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        // Converter entidade para DTO
        return modelMapper.map(pedidoAtualizado, PedidoResponseDTO.class);
    }

    @Override
    public BigDecimal calcularValorTotalPedido(List<ItemPedidoRequestDTO> itens) {
        // Calcular o valor total do pedido somando os preços dos itens
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemPedidoRequestDTO item : itens) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + item.getProdutoId()));
            valorTotal = valorTotal.add(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        return valorTotal;
    }

    @Override
    public PedidoResponseDTO cancelarPedido(Long id) {
        // Buscar pedido por ID
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        // Verificar se o pedido já está cancelado
        if (pedido.getStatus().equals(StatusPedido.CANCELADO.name())) {
            throw new RuntimeException("Pedido já está cancelado: " + id);
        }
        // Atualizar status do pedido para CANCELADO
        podeSerCancelado(StatusPedido.valueOf(pedido.getStatus()));
        if (!podeSerCancelado(StatusPedido.valueOf(pedido.getStatus()))) {
            throw new BusinessException("Pedido não pode ser cancelado, status atual: " + pedido.getStatus());
        }
        pedido.setStatus(StatusPedido.CANCELADO.name());
        // Salvar pedido atualizado
        pedidoRepository.save(pedido);
        // Converter entidade para DTO
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    private boolean isTransicaoValida(StatusPedido statusAtual, StatusPedido novoStatus) {
        // Implementar lógica de transições válidas
        switch (statusAtual) {
            case PENDENTE:
                return novoStatus == StatusPedido.CONFIRMADO || novoStatus == StatusPedido.CANCELADO;
            case CONFIRMADO:
                return novoStatus == StatusPedido.PREPARANDO || novoStatus == StatusPedido.CANCELADO;
            case PREPARANDO:
                return novoStatus == StatusPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA:
                return novoStatus == StatusPedido.ENTREGUE;
            default:
                return false;
        }
    }

    private boolean podeSerCancelado(StatusPedido status) {
        return status == StatusPedido.PENDENTE || status == StatusPedido.CONFIRMADO;
    }
}