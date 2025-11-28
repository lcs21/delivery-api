package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery_api.exception.ConflictException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.services.impl.ClienteServiceImpl;
import org.junit.jupiter.api.Test;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private ModelMapper modelMapper;
    @InjectMocks private ClienteServiceImpl clienteService;

    @Captor private ArgumentCaptor<Cliente> clienteCaptor;


    @Test
    void deveCadastrarClienteValido() {
        var req = new ClienteRequestDTO();
        req.setNome("Lucas");
        req.setEmail("lucas@example.com");
        req.setTelefone("(11) 91234-5678");
        req.setEndereco("Rua X, 123");

        // Evite usar string fixa se o service normaliza o email
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> {
            Cliente c = inv.getArgument(0, Cliente.class);
            c.setId(1L);
            return c;
        });

        ClienteResponseDTO resp = clienteService.cadastrar(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getEmail()).isEqualTo("lucas@example.com");

        verify(clienteRepository).save(clienteCaptor.capture());
        Cliente persistido = clienteCaptor.getValue();
        assertThat(persistido.getNome()).isEqualTo("Lucas");
        assertThat(persistido.getEmail()).isEqualTo("lucas@example.com");
    }


    @Test
    void deveLancarErroAoCadastrarEmailDuplicado() {
        var req = new ClienteRequestDTO();
        req.setNome("Lucas");
        req.setEmail("lucas@example.com");
        req.setTelefone("(11) 91234-5678");
        req.setEndereco("Rua X, 123");


        when(clienteRepository.existsByEmail("lucas@example.com")).thenReturn(true);

        // Use a exceção do seu projeto; pela sua árvore, sugiro ConflictException
        assertThrows(ConflictException.class, () -> clienteService.cadastrar(req));

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveBuscarClientePorIdExistente() {
        var entity = new Cliente();
        entity.setId(10L);
        entity.setNome("Ana");
        entity.setEmail("ana@example.com");

        when(clienteRepository.findById(10L)).thenReturn(Optional.of(entity));

        ClienteResponseDTO resp = clienteService.buscarPorId(10L);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getEmail()).isEqualTo("ana@example.com");

        verify(clienteRepository).findById(10L);
    }

    @Test
    void deveFalharAoBuscarClientePorIdInexistente() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.buscarPorId(999L));

        verify(clienteRepository).findById(999L);
    }



    @Test
    void deveListarClientesAtivos() {
        var c1 = new Cliente(); c1.setId(1L); c1.setNome("Ana"); c1.setEmail("ana@example.com");
        var c2 = new Cliente(); c2.setId(2L); c2.setNome("Bruno"); c2.setEmail("bruno@example.com");

        when(clienteRepository.findByAtivoTrue()).thenReturn(List.of(c1, c2));

        List<ClienteResponseDTO> lista = clienteService.listarAtivos();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getEmail()).isEqualTo("ana@example.com");
        verify(clienteRepository).findByAtivoTrue();
    }

    @Test
    void deveBuscarClientesPorNome() {
        var c1 = new Cliente(); c1.setId(1L); c1.setNome("Lucas"); c1.setEmail("lucas@example.com");

        when(clienteRepository.findByNomeContainingIgnoreCase("luc"))
                .thenReturn(List.of(c1));

        List<ClienteResponseDTO> lista = clienteService.buscarPorNome("luc");

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Lucas");

        verify(clienteRepository).findByNomeContainingIgnoreCase("luc");
    }
}


