package com.deliverytech.delivery_api.services.impl;

import com.deliverytech.delivery_api.dto.request.LoginRequestDTO;
import com.deliverytech.delivery_api.dto.request.UsuarioRequestDTO;
import com.deliverytech.delivery_api.dto.response.LoginResponseDTO;
import com.deliverytech.delivery_api.dto.response.UsuarioResponseDTO;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
//@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              @Lazy PasswordEncoder passwordEncoder,
                              @Lazy AuthenticationManager authenticationManager,
                              JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if(usuarioRepository.existsByEmail(dto.getEmail())){
            throw new BusinessException("Email já cadastrado: " + dto.getEmail());
        }
        Usuario usuario = Usuario.builder()
                .email(dto.getEmail())
                .nome(dto.getNome())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(dto.getRole())
                .restauranteId(dto.getRestauranteId())
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();
        usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioResponseDTO.class);

    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Usuário inválido", String.valueOf(HttpStatus.UNAUTHORIZED)));

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setUsuario(modelMapper.map(usuario, UsuarioResponseDTO.class));
        responseDTO.setTipo("Bearer");
        responseDTO.setExpiracao(86400000L); // 1 dia em segundos
        responseDTO.setToken(jwtUtil.generateToken(User.withUsername(usuario.getEmail()).password(usuario.getSenha()).authorities("ROLE_" + usuario.getRole().name()).build(), usuario));

        return responseDTO;

    }

    // ... existing code ...

    // Adicione este método para cumprir o contrato da interface UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ajuste o método "findByEmail" conforme o nome exato no seu UsuarioRepository
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Retorna o objeto User do Spring Security
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                new ArrayList<>() // Aqui você passaria as roles/authorities se tivesse
        );
    }
}