
package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.controller.TestDataConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class ClienteControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ApplicationContext ctx;
    @Autowired ClienteRepository clienteRepository;

    @BeforeAll
    static void seed(@Autowired ApplicationContext ctx) {
        ctx.getBean(Runnable.class).run();
    }

    @Test
    void deveCriarClienteComDadosValidos() throws Exception {
        var payload = """
      { "nome": "Lucas", "email": "lucas@example.com", "telefone": "(11) 91234-5678", "endereco": "Rua X, 123" }
    """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("lucas@example.com"));
    }

    @Test
    void deveRejeitarClienteComEmailInvalido() throws Exception {
        var payload = """
      { "nome": "Zé", "email": "invalido", "telefone": "(11) 91234-5678", "endereco": "Rua X, 123" }
    """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", not(empty())));
    }

    @Test
    void deveBuscarClientePorIdExistente() throws Exception {
        // ID semeado (busque o primeiro existente)
        var id = clienteRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email", not(emptyString())));
    }

    @Test
    void deveRetornar404ParaClienteInexistente() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deveListarClientesComPaginacao() throws Exception {
        mockMvc.perform(get("/api/clientes")
                        .param("pagina", "0")
                        .param("tamanho", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(2)));
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        var id = clienteRepository.findAll().get(0).getId();

        var payload = """
      { "nome": "Ana Maria", "email": "ana@example.com", "telefone": "(11) 95555-0001", "endereco": "Rua Nova, 50" }
    """;

        mockMvc.perform(put("/api/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Maria"));
    }
}
