package com.deliverytech.delivery_api.services;


import com.deliverytech.delivery_api.dto.request.LoginRequestDTO;
import com.deliverytech.delivery_api.dto.request.UsuarioRequestDTO;
import com.deliverytech.delivery_api.dto.response.LoginResponseDTO;
import com.deliverytech.delivery_api.dto.response.UsuarioResponseDTO;

public interface UsuarioService {

    UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

}