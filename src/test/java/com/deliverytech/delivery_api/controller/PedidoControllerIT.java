
package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.controller.TestDataConfig;
import com.jayway.jsonpath.JsonPath;
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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class PedidoControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ApplicationContext ctx;
    @Autowired ClienteRepository clienteRepository;
    @Autowired ProdutoRepository produtoRepository;
    @Autowired PedidoRepository pedidoRepository;

    @BeforeAll
    static void seed(@Autowired ApplicationContext ctx) {
        ctx.getBean(Runnable.class).run();
    }

    @Test
    void deveCriarPedidoCompleto() throws Exception {
        var clienteId = clienteRepository.findAll().get(0).getId();
        var p1 = produtoRepository.findAll().stream().filter(Produto -> Produto.getDisponivel()).findFirst().orElseThrow();
        var p2 = produtoRepository.findAll().stream().filter(Produto -> Produto.getDisponivel()).skip(1).findFirst().orElseThrow();

        var payload = String.format("""
      {
        "clienteId": %d,
        "itens": [
          { "produtoId": %d, "quantidade": 1 },
          { "produtoId": %d, "quantidade": 1 }
        ]
      }
    """, clienteId, p1.getId(), p2.getId());

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valorTotal").value(closeTo(p1.getPreco().add(p2.getPreco()).doubleValue(), 0.01)))
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

    @Test
    void deveFalharComProdutoInexistente() throws Exception {
        var clienteId = clienteRepository.findAll().get(0).getId();

        var payload = String.format("""
      {
        "clienteId": %d,
        "itens": [
          { "produtoId": 999999, "quantidade": 1 }
        ]
      }
    """, clienteId);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deveFalharComProdutoIndisponivel() throws Exception {
        var clienteId = clienteRepository.findAll().get(0).getId();
        var indisponivel = produtoRepository.findAll().stream()
                .filter(p -> Boolean.FALSE.equals(p.getDisponivel()))
                .findFirst().orElseThrow();

        var payload = String.format("""
      {
        "clienteId": %d,
        "itens": [
          { "produtoId": %d, "quantidade": 1 }
        ]
      }
    """, clienteId, indisponivel.getId());

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deveListarHistoricoDoCliente() throws Exception {
        var clienteId = clienteRepository.findAll().get(0).getId();

        // cria um pedido para garantir ao menos 1 item no histórico
        var produtoId = produtoRepository.findAll().stream().filter(p -> p.getDisponivel()).findFirst().orElseThrow().getId();
        var novoPedido = String.format("""
      { "clienteId": %d, "itens": [ { "produtoId": %d, "quantidade": 1 } ] }
    """, clienteId, produtoId);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoPedido))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/pedidos/cliente/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].clienteId").value(clienteId));
    }

    @Test
    void deveAtualizarStatusDoPedido() throws Exception {
        //  pedido
        var clienteId = clienteRepository.findAll().get(0).getId();
        var produtoId = produtoRepository.findAll().stream().filter(p -> p.getDisponivel()).findFirst().orElseThrow().getId();

        var novo = String.format("""
      { "clienteId": %d, "itens": [ { "produtoId": %d, "quantidade": 1 } ] }
    """, clienteId, produtoId);

        var created = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novo))
                .andExpect(status().isCreated())
                .andReturn();

        var id = JsonPath.read(created.getResponse().getContentAsString(), "$.id"); // use JsonPath se disponível; senão extraia por Jackson

        var statusPayload = """
      { "status": "PREPARANDO" }
    """;

        mockMvc.perform(put("/api/pedidos/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusPedido.PREPARANDO.getDescricao()));
    }
}