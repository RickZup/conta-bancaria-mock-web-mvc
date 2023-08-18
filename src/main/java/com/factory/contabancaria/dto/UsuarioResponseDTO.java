package com.factory.contabancaria.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UsuarioResponseDTO {

    //Método GET

    private String numConta;

    private String agencia;

    private String nomeDoUsuario;

    private BigDecimal valorAtualConta;

}
