package com.deliverytech.delivery_api.controller;
import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.response.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.services.PedidoService;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Criar pedido",
            description = "Cria um novo pedido no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe")
    })
    public ResponseEntity<PedidoResponseDTO> criarPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO pedido = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID",
            description = "Recupera os detalhes de um pedido específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente",
            description = "Lista todos os pedidos de um cliente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido",
            description = "Cancela um pedido específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedido);
    }
    @PostMapping("/calcular")
    @Operation(summary = "Calcular valor total do pedido",
            description = "Calcula o valor total de um pedido com base nos itens fornecidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor total calculado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<BigDecimal> calcularValorTotalPedido(@RequestBody List<ItemPedidoRequestDTO> itens) {
        BigDecimal valorTotal = pedidoService.calcularValorTotalPedido(itens);
        return ResponseEntity.ok(valorTotal);
    }

    @PutMapping("/{id}/{status}")
    @Operation(summary = "Atualizar status do pedido",
            description = "Atualiza o status de um pedido específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @PathVariable StatusPedido status) {
        PedidoResponseDTO dto = pedidoService.atualizarStatusPedido(id, status);
        return ResponseEntity.ok(dto);
    }
}