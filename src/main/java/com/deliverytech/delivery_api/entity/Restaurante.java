package com.deliverytech.delivery_api.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurantes")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String categoria;

    private String endereco;

    private String cep;

    private String telefone;

    @Column(name = "taxa_entrega")
    private BigDecimal taxaEntrega;

    private BigDecimal avaliacao;

    private Boolean ativo;

    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Produto> produtos;

    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos;

    public boolean isAtivo() {
        return this.ativo != null && this.ativo;
    }

}