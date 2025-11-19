package com.deliverytech.delivery_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo;
    private Long expiracao;
    private UsuarioResponseDTO usuario;
}