package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private RestauranteRequestDTO restauranteRequestDTO;
    private Restaurante restauranteSalvo;

    @BeforeEach
    void setUp() {
        restauranteRequestDTO = new RestauranteRequestDTO();
        restauranteRequestDTO.setNome("Restaurante Teste");
        restauranteRequestDTO.setCategoria("Categoria Teste");
        restauranteRequestDTO.setEndereco("Endereço Teste");
        restauranteRequestDTO.setTelefone("123456789");
        restauranteRequestDTO.setTaxaEntrega(BigDecimal.valueOf(5.00));
        restauranteRequestDTO.setAvaliacao(BigDecimal.valueOf(5.00));
        restauranteRequestDTO.setAtivo(true);

        // Salva um restaurante para testes
        restauranteSalvo = new Restaurante();
        restauranteSalvo.setNome(restauranteRequestDTO.getNome());
        restauranteSalvo.setCategoria(restauranteRequestDTO.getCategoria());
        restauranteSalvo.setEndereco(restauranteRequestDTO.getEndereco());
        restauranteSalvo.setTelefone(restauranteRequestDTO.getTelefone());
        restauranteSalvo.setTaxaEntrega(restauranteRequestDTO.getTaxaEntrega());
        restauranteSalvo.setAvaliacao(restauranteRequestDTO.getAvaliacao());
        restauranteSalvo.setAtivo(restauranteRequestDTO.isAtivo());
        restauranteSalvo = restauranteRepository.save(restauranteSalvo);
    }

//    @Test
//    void cadastrar() {
//        try {
//            String jsonRequest = objectMapper.writeValueAsString(restauranteRequestDTO);
//            mockMvc.perform(post("/restaurantes")
//                    .contentType("application/json")
//                    .content(jsonRequest))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.nome").value(restauranteRequestDTO.getNome()))
//                    .andExpect(jsonPath("$.categoria").value(restauranteRequestDTO.getCategoria()))
//                    .andExpect(jsonPath("$.endereco").value(restauranteRequestDTO.getEndereco()))
//                    .andExpect(jsonPath("$.telefone").value(restauranteRequestDTO.getTelefone()))
//                    .andExpect(jsonPath("$.taxaEntrega").value(restauranteRequestDTO.getTaxaEntrega().doubleValue()))
//                    .andExpect(jsonPath("$.avaliacao").value(restauranteRequestDTO.getAvaliacao().doubleValue()))
//                    .andExpect(jsonPath("$.ativo").value(restauranteRequestDTO.isAtivo()))
//                    .andExpect(jsonPath("$.message").value("Restaurante criado com sucesso"));
//        } catch (Exception e) {
//            fail("Erro ao cadastrar restaurante: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void listarTodos() {
//        try {
//            mockMvc.perform(post("/restaurantes"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.data[0].nome").value(restauranteSalvo.getNome()))
//                    .andExpect(jsonPath("$.data[0].categoria").value(restauranteSalvo.getCategoria()))
//                    .andExpect(jsonPath("$.data[0].endereco").value(restauranteSalvo.getEndereco()))
//                    .andExpect(jsonPath("$.data[0].telefone").value(restauranteSalvo.getTelefone()))
//                    .andExpect(jsonPath("$.data[0].taxaEntrega").value(restauranteSalvo.getTaxaEntrega().doubleValue()))
//                    .andExpect(jsonPath("$.data[0].ativo").value(restauranteSalvo.isAtivo()));
//        } catch (Exception e) {
//            fail("Erro ao listar restaurantes: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void buscarPorId() {
//        try {
//            mockMvc.perform(post("/restaurantes/" + restauranteSalvo.getId()))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.nome").value(restauranteSalvo.getNome()))
//                    .andExpect(jsonPath("$.categoria").value(restauranteSalvo.getCategoria()))
//                    .andExpect(jsonPath("$.endereco").value(restauranteSalvo.getEndereco()))
//                    .andExpect(jsonPath("$.telefone").value(restauranteSalvo.getTelefone()))
//                    .andExpect(jsonPath("$.taxaEntrega").value(restauranteSalvo.getTaxaEntrega().doubleValue()));
//        } catch (Exception e) {
//            fail("Erro ao buscar restaurante por ID: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void atualizar() {
//        try {
//            restauranteRequestDTO.setNome("Restaurante Atualizado");
//            String jsonRequest = objectMapper.writeValueAsString(restauranteRequestDTO);
//            mockMvc.perform(post("/restaurantes/" + restauranteSalvo.getId())
//                    .contentType("application/json")
//                    .content(jsonRequest))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.nome").value("Restaurante Atualizado"))
//                    .andExpect(jsonPath("$.categoria").value(restauranteRequestDTO.getCategoria()))
//                    .andExpect(jsonPath("$.endereco").value(restauranteRequestDTO.getEndereco()))
//                    .andExpect(jsonPath("$.telefone").value(restauranteRequestDTO.getTelefone()))
//                    .andExpect(jsonPath("$.taxaEntrega").value(restauranteRequestDTO.getTaxaEntrega().doubleValue()));
//        } catch (Exception e) {
//            fail("Erro ao atualizar restaurante: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void ativarDesativarRestaurante() {
//        try {
//            mockMvc.perform(post("/restaurantes/" + restauranteSalvo.getId() + "/ativar-desativar"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.ativo").value(!restauranteSalvo.isAtivo()));
//        } catch (Exception e) {
//            fail("Erro ao ativar/desativar restaurante: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void buscarPorNome() {
//        try {
//            mockMvc.perform(post("/restaurantes/nome/" + restauranteSalvo.getNome()))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.nome").value(restauranteSalvo.getNome()))
//                    .andExpect(jsonPath("$.categoria").value(restauranteSalvo.getCategoria()))
//                    .andExpect(jsonPath("$.endereco").value(restauranteSalvo.getEndereco()))
//                    .andExpect(jsonPath("$.telefone").value(restauranteSalvo.getTelefone()))
//                    .andExpect(jsonPath("$.taxaEntrega").value(restauranteSalvo.getTaxaEntrega().doubleValue()));
//        } catch (Exception e) {
//            fail("Erro ao buscar restaurante por nome: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void buscarPorPreco() {
//        try {
//            BigDecimal precoMinimo = BigDecimal.valueOf(0);
//            BigDecimal precoMaximo = BigDecimal.valueOf(10);
//            mockMvc.perform(post("/restaurantes/preco/" + precoMinimo + "/" + precoMaximo))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.data[0].nome").value(restauranteSalvo.getNome()))
//                    .andExpect(jsonPath("$.data[0].categoria").value(restauranteSalvo.getCategoria()))
//                    .andExpect(jsonPath("$.data[0].endereco").value(restauranteSalvo.getEndereco()))
//                    .andExpect(jsonPath("$.data[0].telefone").value(restauranteSalvo.getTelefone()))
//                    .andExpect(jsonPath("$.data[0].taxaEntrega").value(restauranteSalvo.getTaxaEntrega().doubleValue()));
//        } catch (Exception e) {
//            fail("Erro ao buscar restaurantes por faixa de preço: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void buscarPorCategoria() {
//        try {
//            mockMvc.perform(post("/restaurantes/categoria/" + restauranteSalvo.getCategoria()))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.data[0].nome").value(restauranteSalvo.getNome()))
//                    .andExpect(jsonPath("$.data[0].categoria").value(restauranteSalvo.getCategoria()))
//                    .andExpect(jsonPath("$.data[0].endereco").value(restauranteSalvo.getEndereco()))
//                    .andExpect(jsonPath("$.data[0].telefone").value(restauranteSalvo.getTelefone()))
//                    .andExpect(jsonPath("$.data[0].taxaEntrega").value(restauranteSalvo.getTaxaEntrega().doubleValue()));
//        } catch (Exception e) {
//            fail("Erro ao buscar restaurantes por categoria: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void inativarRestaurante() {
//        try {
//            mockMvc.perform(post("/restaurantes/" + restauranteSalvo.getId() + "/inativar"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.ativo").value(false));
//        } catch (Exception e) {
//            fail("Erro ao inativar restaurante: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void buscarPorTaxaEntrega() {
//        try {
//            BigDecimal taxaEntrega = BigDecimal.valueOf(5.00);
//            mockMvc.perform(post("/restaurantes/taxa-entrega?taxa=" + taxaEntrega))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.data[0].nome").value(restauranteSalvo.getNome()))
//                    .andExpect(jsonPath("$.data[0].categoria").value(restauranteSalvo.getCategoria()))
//                    .andExpect(jsonPath("$.data[0].endereco").value(restauranteSalvo.getEndereco()))
//                    .andExpect(jsonPath("$.data[0].telefone").value(restauranteSalvo.getTelefone()))
//                    .andExpect(jsonPath("$.data[0].taxaEntrega").value(restauranteSalvo.getTaxaEntrega().doubleValue()));
//        } catch (Exception e) {
//            fail("Erro ao buscar restaurantes por taxa de entrega: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void listarTop5PorNome() {
//        try {
//            mockMvc.perform(post("/restaurantes/top-cinco"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.data[0].nome").value(restauranteSalvo.getNome()))
//                    .andExpect(jsonPath("$.data[0].categoria").value(restauranteSalvo.getCategoria()))
//                    .andExpect(jsonPath("$.data[0].endereco").value(restauranteSalvo.getEndereco()))
//                    .andExpect(jsonPath("$.data[0].telefone").value(restauranteSalvo.getTelefone()))
//                    .andExpect(jsonPath("$.data[0].taxaEntrega").value(restauranteSalvo.getTaxaEntrega().doubleValue()));
//        } catch (Exception e) {
//            fail("Erro ao listar os 5 restaurantes mais populares por nome: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void relatorioVendasPorRestaurante() {
//        try {
//            mockMvc.perform(post("/restaurantes/relatorio-vendas"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data").isArray());
//            // Aqui você pode adicionar mais verificações dependendo do formato esperado do relatório
//        } catch (Exception e) {
//            fail("Erro ao gerar relatório de vendas por restaurante: " + e.getMessage());
//        }
//    }
}