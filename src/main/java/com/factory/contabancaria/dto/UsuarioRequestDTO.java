package com.factory.contabancaria.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UsuarioRequestDTO {

    //Método POST

    private String nomeDoUsuario;

    private BigDecimal valorAtualConta;

    private BigDecimal ValorFornecido;

    private String tipoServico;

}
