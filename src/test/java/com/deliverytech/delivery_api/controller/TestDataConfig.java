package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@TestConfiguration
public class TestDataConfig {

    @Bean
    public Runnable seedData(ClienteRepository clienteRepo, ProdutoRepository produtoRepo) {
        return () -> {
            // Clientes
            Cliente c1 = new Cliente(); c1.setNome("Ana");   c1.setEmail("ana@example.com");   c1.setTelefone("(11) 90000-0001"); c1.setEndereco("Rua A, 10");
            Cliente c2 = new Cliente(); c2.setNome("Bruno"); c2.setEmail("bruno@example.com"); c2.setTelefone("(11) 90000-0002"); c2.setEndereco("Rua B, 20");
            clienteRepo.save(c1); clienteRepo.save(c2);

            // Produtos disponíveis
            Produto p1 = new Produto(); p1.setNome("Pizza");  p1.setDescricao("Calabresa"); p1.setPreco(new BigDecimal("39.90")); p1.setCategoria("Comida"); p1.setDisponivel(true);
            Produto p2 = new Produto(); p2.setNome("Refri");  p2.setDescricao("Cola");      p2.setPreco(new BigDecimal("9.90"));  p2.setCategoria("Bebida"); p2.setDisponivel(true);
            Produto p3 = new Produto(); p3.setNome("Suco");   p3.setDescricao("Uva");       p3.setPreco(new BigDecimal("8.00"));  p3.setCategoria("Bebida"); p3.setDisponivel(false); // para cenário de indisponível
            produtoRepo.save(p1); produtoRepo.save(p2); produtoRepo.save(p3);
        };
    }
}
