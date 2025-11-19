package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.validation.ValidCEP;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDTO {
    @Schema(description = "Número do pedido", example = "12345", required = true)
    @NotNull(message = "O número do pedido é obrigatório")
    private String numeroPedido;

    @Schema(description = "Data do pedido", example = "2023-10-01", required = true)
    @NotNull(message = "A data do pedido é obrigatória")
    private String dataPedido;

    @Schema(description = "Valor total do pedido", example = "99.99", required = true)
    @NotNull(message = "O valor do pedido é obrigatório")
    private BigDecimal valorTotal;

    @Schema(description = "Observações do pedido", example = "Não colocar cebola")
    private String observacoes;

    @Schema(description = "Status do pedido", example = "PENDENTE", required = true)
    @NotNull(message = "O status do pedido é obrigatório")
    private Long clienteId;

    @Schema(description = "ID do restaurante", example = "1", required = true)
    @NotNull(message = "O restaurante é obrigatório")
    private Long restauranteId;

    @Schema(description = "Endereço de entrega do pedido", example = "Rua das Flores, 123")
    private String enderecoEntrega;

    @NotBlank(message = "CEP é obrigatório")
    @ValidCEP
    private String cep;

    @Schema(description = "Lista de itens do pedido", required = true)
    @NotEmpty(message = "Os itens são obrigatórios")
    @Valid
    private List<ItemPedidoRequestDTO> itens;
}