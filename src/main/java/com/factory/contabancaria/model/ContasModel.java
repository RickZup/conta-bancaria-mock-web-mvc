package com.factory.contabancaria.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_CONTAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContasModel {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 6, nullable = false)
    private String numConta;

    @Column(length = 4, nullable = false)
    private String agencia;

    @Column(nullable = false)
    private String nomeDoUsuario;

    @Column(nullable = false)
    private BigDecimal valorAtualConta;

    @Column(nullable = false)
    private BigDecimal ValorFornecido;

    @Column(length = 20, nullable = false)
    private String tipoServico;

    private BigDecimal valorFinal;

}
