package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO para requisição de itens do pedido",
        title = "Item Pedido Request DTO")
public class ItemPedidoRequestDTO {
    @Schema(description = "ID do produto", example = "1", required = true)
    @NotNull(message = "O produto é obrigatório")
    @Positive(message = "Produto ID deve ser positivo")
    private Long produtoId;

    @Schema(description = "Quantidade do produto no pedido", example = "2", required = true)
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    @Max(value = 10, message = "A quantidade não pode ser maior que 100")
    private Integer quantidade;

}