package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {
    private String nome;
    private String email;
    private String senha;
    private Long restauranteId;
    private Role role;
}