package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.entity.Cliente;
import lombok.Data;

@Data
public class ClienteResponseDTO {

    private Long id;

    private String nome;

    private String email;

    private String telefone;

    private String endereco;

    private Boolean ativo;
}