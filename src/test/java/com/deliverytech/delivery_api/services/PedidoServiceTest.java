
package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.services.impl.PedidoServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private ProdutoRepository produtoRepository;

    @InjectMocks private PedidoServiceImpl pedidoService;

    @Captor private ArgumentCaptor<Pedido> pedidoCaptor;

    @Test
    void deveCriarPedidoComItensValidos() {
        // Itens
        var i1 = new ItemPedidoRequestDTO();
        i1.setProdutoId(1L);
        i1.setQuantidade(1);

        var i2 = new ItemPedidoRequestDTO();
        i2.setProdutoId(2L);
        i2.setQuantidade(1);

        var dto = new PedidoRequestDTO();
        dto.setClienteId(999L);
        dto.setItens(List.of(i1, i2));

        var p1 = new Produto();
        p1.setId(1L);
        p1.setNome("Pizza");
        p1.setPreco(new BigDecimal("39.90"));
        p1.setDisponivel(true);

        var p2 = new Produto();
        p2.setId(2L);
        p2.setNome("Refri");
        p2.setPreco(new BigDecimal("9.90"));
        p2.setDisponivel(true);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(p2));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido salvo = inv.getArgument(0, Pedido.class);
            salvo.setId(100L);
            return salvo;
        });

        PedidoResponseDTO result = pedidoService.criarPedido(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);

        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido persistido = pedidoCaptor.getValue();

        assertThat(persistido.getValorTotal()).isEqualByComparingTo(new BigDecimal("49.80"));
        assertThat(persistido.getStatus()).isIn(StatusPedido.PENDENTE, StatusPedido.CONFIRMADO);
    }

    @Test
    void deveFalharAoCriarPedidoComProdutoIndisponivel() {
        var item = new ItemPedidoRequestDTO();
        item.setProdutoId(1L);
        item.setQuantidade(1);

        var dto = new PedidoRequestDTO();
        dto.setClienteId(999L);
        dto.setItens(List.of(item));

        var indisponivel = new Produto();
        indisponivel.setId(1L);
        indisponivel.setNome("Pizza");
        indisponivel.setPreco(new BigDecimal("39.90"));
        indisponivel.setDisponivel(false);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(indisponivel));

        assertThrows(BusinessException.class, () -> pedidoService.criarPedido(dto));

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void deveCalcularValorTotalCorretamente() {
        var i1 = new ItemPedidoRequestDTO(); i1.setProdutoId(1L); i1.setQuantidade(2); // 2 x 25.00
        var i2 = new ItemPedidoRequestDTO(); i2.setProdutoId(2L); i2.setQuantidade(3); // 3 x 12.50
        var i3 = new ItemPedidoRequestDTO(); i3.setProdutoId(3L); i3.setQuantidade(1); // 1 x 8.00

        var p1 = new Produto(); p1.setId(1L); p1.setPreco(new BigDecimal("25.00")); p1.setDisponivel(true);
        var p2 = new Produto(); p2.setId(2L); p2.setPreco(new BigDecimal("12.50")); p2.setDisponivel(true);
        var p3 = new Produto(); p3.setId(3L); p3.setPreco(new BigDecimal("8.00"));  p3.setDisponivel(true);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(p2));
        when(produtoRepository.findById(3L)).thenReturn(Optional.of(p3));

        BigDecimal total = pedidoService.calcularValorTotalPedido(List.of(i1, i2, i3));

        assertThat(total).isEqualByComparingTo(new BigDecimal("95.50"));
    }

    @Test
    void deveAtualizarStatusDoPedido() {
        var pedido = new Pedido();
        pedido.setId(200L);
        pedido.setStatus(StatusPedido.PENDENTE.name());

        when(pedidoRepository.findById(200L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(inv -> inv.getArgument(0, Pedido.class));

        PedidoResponseDTO atualizado =
                pedidoService.atualizarStatusPedido(200L, StatusPedido.PREPARANDO);

        assertThat(atualizado.getStatus())
                .isEqualTo(StatusPedido.PREPARANDO.getDescricao());

        verify(pedidoRepository).findById(200L);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void deveManterConsistenciaAoFalharNaPersistencia() {
        var item = new ItemPedidoRequestDTO(); item.setProdutoId(1L); item.setQuantidade(1);

        var dto = new PedidoRequestDTO();
        dto.setClienteId(999L);
        dto.setItens(List.of(item));

        var p1 = new Produto(); p1.setId(1L); p1.setPreco(new BigDecimal("39.90")); p1.setDisponivel(true);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(pedidoRepository.save(any(Pedido.class))).thenThrow(new RuntimeException("Falha ao persistir"));

        assertThrows(RuntimeException.class, () -> pedidoService.criarPedido(dto));

        verify(produtoRepository).findById(1L);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void deveLancarEntityNotFoundAoAtualizarStatusDePedidoInexistente() {
        when(pedidoRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> pedidoService.atualizarStatusPedido(404L, StatusPedido.SAIU_PARA_ENTREGA));

        verify(pedidoRepository).findById(404L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}
