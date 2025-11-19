package com.deliverytech.delivery_api.services.impl;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.Mockito.eq;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DisplayName("Testes Unitário Cliente Service")
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private ModelMapper modelMapper;

    private Cliente cliente;

    private ClienteRequestDTO clienteRequestDTO;

    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();

        // Inicializando o DTO de requisição
        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setNome("Maria Silva");
        clienteRequestDTO.setEmail("maria@email.com");
        clienteRequestDTO.setTelefone("11999999999");
        clienteRequestDTO.setEndereco("Rua Exemplo, 123, São Paulo, SP");
    }

    @Test
    @DisplayName("Deve salvar cliente com dados válidos")
    void cadastrar() {
        //Given
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        // No teste ClienteServiceImplTest.java
        when(modelMapper.map(any(ClienteRequestDTO.class), eq(Cliente.class)))
                .thenReturn(new Cliente());
        // When
        clienteResponseDTO = clienteService.cadastrar(clienteRequestDTO);

        // Then
        assertNotNull(clienteRequestDTO);
        assertEquals("Maria Silva", clienteRequestDTO.getNome());
        assertEquals("maria@email.com", clienteRequestDTO.getEmail());
        verify(clienteRepository).save(any(Cliente.class));
        verify(clienteRepository).existsByEmail("maria@email.com");
    }

    @Test
    @DisplayName("Deve atualizar cliente com dados válidos")
    void atualizar() {
        // Given
        Long clienteId = 1L;
        cliente.setId(clienteId);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setNome("Maria Silva");
        cliente.setEmail("maria@maria.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Exemplo, 123, São Paulo, SP");
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
                .thenReturn(new ClienteResponseDTO() {{
                    setNome("Maria Silva Atualizado");
                    setEmail("maria@maria.com");
                    setTelefone("11999999999");
                    setEndereco("Rua Exemplo, 123, São Paulo, SP");
                    setAtivo(true);
                }});

        // When
        ClienteResponseDTO responseDTO = clienteService.atualizar(clienteId, clienteRequestDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Maria Silva Atualizado", responseDTO.getNome());
        assertEquals("maria@maria.com", responseDTO.getEmail());
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).save(any(Cliente.class));
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));

    }

    @Test
    @DisplayName("Deve ativar/desativar cliente")
    void ativarDesativarCliente() {
        // Given
        Long clienteId = 1L;
        cliente.setId(clienteId);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setNome("Maria Silva");
        cliente.setEmail("maria@maria.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Exemplo, 123, São Paulo, SP");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
                .thenReturn(new ClienteResponseDTO() {{
                    setNome("Maria Silva");
                    setEmail("maria@maria.com");
                    setTelefone("11999999999");
                    setEndereco("Rua Exemplo, 123, São Paulo, SP");
                    setAtivo(false); // Inverte o status de ativo
                }});
        // When
        ClienteResponseDTO responseDTO = clienteService.ativarDesativarCliente(clienteId);
        // Then
        assertNotNull(responseDTO);
        assertEquals("Maria Silva", responseDTO.getNome());
        assertEquals("maria@maria.com", responseDTO.getEmail());
        assertFalse(responseDTO.isAtivo()); // Verifica se o status foi invertido
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).save(any(Cliente.class));
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));

    }

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void buscarPorId() {
        // Given
        Long clienteId = 1L;
        cliente.setId(clienteId);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setNome("Maria Silva");
        cliente.setEmail("maria@maria.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Exemplo, 123, São Paulo, SP");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
                .thenReturn(new ClienteResponseDTO() {{
                    setNome("Maria Silva");
                    setEmail("maria@maria.com");
                    setTelefone("11999999999");
                    setEndereco("Rua Exemplo, 123, São Paulo, SP");
                    setAtivo(true);
                }});
        // When
        ClienteResponseDTO responseDTO = clienteService.buscarPorId(clienteId);
        // Then
        assertNotNull(responseDTO);
        assertEquals("Maria Silva", responseDTO.getNome());
        assertEquals("maria@maria.com", responseDTO.getEmail());
        assertTrue(responseDTO.isAtivo());
        verify(clienteRepository).findById(clienteId);
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));

    }

    @Test
    @DisplayName("Deve buscar cliente por email")
    void buscarPorEmail() {
        // Given
        String email = "maria@maria.com";
        cliente.setEmail(email);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setNome("Maria Silva");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Exemplo, 123, São Paulo, SP");

        when(clienteRepository.findByEmail(email)).thenReturn(Optional.of(cliente));
        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
                .thenReturn(new ClienteResponseDTO() {{
                    setNome("Maria Silva");
                    setEmail("maria@maria.com");
                    setTelefone("11999999999");
                    setEndereco("Rua Exemplo, 123, São Paulo, SP");
                    setAtivo(true);
                }});
        // When
        ClienteResponseDTO responseDTO = clienteService.buscarPorEmail(email);
        // Then
        assertNotNull(responseDTO);
        assertEquals("Maria Silva", responseDTO.getNome());
        assertEquals("maria@maria.com", responseDTO.getEmail());
        assertTrue(responseDTO.isAtivo());
        verify(clienteRepository).findByEmail(email);
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));

    }

    @Test
    @DisplayName("Deve listar todos os clientes ativos")
    void listarAtivos() {
        // Given
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setNome("Maria Silva");
        cliente.setEmail("maria@maria.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Exemplo, 123, São Paulo, SP");

        when(clienteRepository.findByAtivoTrue()).thenReturn(List.of(cliente));
        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
                .thenReturn(new ClienteResponseDTO() {{
                    setNome("Maria Silva");
                    setEmail("maria@maria.com");
                    setTelefone("11999999999");
                    setEndereco("Rua Exemplo, 123, São Paulo, SP");
                    setAtivo(true);
                }});
        // When
        var responseList = clienteService.listarAtivos();
        // Then
        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        assertEquals("Maria Silva", responseList.get(0).getNome());
        assertEquals("maria@maria.com", responseList.get(0).getEmail());
        assertTrue(responseList.get(0).isAtivo());
        verify(clienteRepository).findByAtivoTrue();
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));

    }


    @Test
    @DisplayName("Deve listar clientes por nome")
    void buscarPorNome() {
        // Given
        String nome = "Maria";
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setNome("Maria Silva");
        cliente.setEmail("maria@maria.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Exemplo, 123, São Paulo, SP");

        when(clienteRepository.findByNomeContainingIgnoreCase(nome)).thenReturn(List.of(cliente));
        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
                .thenReturn(new ClienteResponseDTO() {{
                    setNome("Maria Silva");
                    setEmail("maria@maria.com");
                    setTelefone("11999999999");
                    setEndereco("Rua Exemplo, 123, São Paulo, SP");
                    setAtivo(true);
                }});
        // When
        List<ClienteResponseDTO> responseList = clienteService.buscarPorNome(nome);
        // Then
        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        assertEquals("Maria Silva", responseList.get(0).getNome());
        assertEquals("maria@maria.com", responseList.get(0).getEmail());
        assertTrue(responseList.get(0).isAtivo());
        verify(clienteRepository).findByNomeContainingIgnoreCase(nome);
        verify(modelMapper).map(any(Cliente.class), eq(ClienteResponseDTO.class));

    }
}