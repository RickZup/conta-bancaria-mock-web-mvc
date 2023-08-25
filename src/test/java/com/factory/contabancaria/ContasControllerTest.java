package com.factory.contabancaria;
import com.factory.contabancaria.controller.ContasController;
import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.factory.ContaFactory;
import com.factory.contabancaria.repository.ContasRepository;
import com.factory.contabancaria.service.ContasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContasController.class)
public class ContasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContasService contasService;

    @MockBean
    private ContasRepository contasRepository;

    @MockBean
    ContaFactory contaFactory;

    @Test
    public void testListarTodasContas() throws Exception {

        ContasModel conta1 = new ContasModel(1L,"1040", "0001", "Ricardo", new BigDecimal(2500),
                                                new BigDecimal(500), "DEPOSITO", new BigDecimal(3000));

        ContasModel conta2 = new ContasModel(2L,"6126", "0002", "Henrique", new BigDecimal(3500),
                new BigDecimal(600), "DEPOSITO", new BigDecimal(4100));


        List<ContasModel> contasList = Arrays.asList(conta1, conta2);
        when(contasService.listarContas()).thenReturn(contasList);


        mockMvc.perform(get("/api/contas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numConta").value(conta1.getNumConta()))
                .andExpect(jsonPath("$[0].agencia").value(conta1.getAgencia()))
                .andExpect(jsonPath("$[0].nomeDoUsuario").value(conta1.getNomeDoUsuario()))
                .andExpect(jsonPath("$[0].valorAtualConta").value(conta1.getValorAtualConta()))
                .andExpect(jsonPath("$[1].numConta").value(conta2.getNumConta()))
                .andExpect(jsonPath("$[1].agencia").value(conta2.getAgencia()))
                .andExpect(jsonPath("$[1].nomeDoUsuario").value(conta2.getNomeDoUsuario()))
                .andExpect(jsonPath("$[1].valorAtualConta").value(conta2.getValorAtualConta()));
    }

    @Test
    public void testExibeContaPorId() throws Exception {

        ContasModel contaSimulada = new ContasModel();
        contaSimulada.setId(1L);
        contaSimulada.setNomeDoUsuario("Ricardo");

        when(contasService.exibeContaPorId(1L)).thenReturn(Optional.of(contaSimulada));

        mockMvc.perform(get("/api/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeDoUsuario").value("Ricardo"));
    }

    @Test
    public void testExibirContaPorNomeDoUsuario() throws Exception {

        ContasModel contaSimulada = new ContasModel();
        contaSimulada.setNumConta("12345");
        contaSimulada.setAgencia("6789");
        contaSimulada.setNomeDoUsuario("Ricardo");
        contaSimulada.setValorAtualConta(BigDecimal.valueOf(1000.0));

        when(contasService.exibirContaPorNomeDoUsuario("Ricardo")).thenReturn(contaSimulada);

        mockMvc.perform(get("/api/contas/buscarPorNome/Ricardo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numConta").value("12345"))
                .andExpect(jsonPath("$.agencia").value("6789"))
                .andExpect(jsonPath("$.nomeDoUsuario").value("Ricardo"))
                .andExpect(jsonPath("$.valorAtualConta").value(1000.0));
    }

    @Test
    public void testCadastrarConta() throws Exception {

        ContasModel contaSimulada = new ContasModel();
        contaSimulada.setNomeDoUsuario("Nome do Usuário");
        contaSimulada.setValorAtualConta(BigDecimal.valueOf(1000.0));
        contaSimulada.setValorFornecido(BigDecimal.valueOf(1000.0));
        contaSimulada.setTipoServico("Serviço Simulado");

        when(contasService.cadastrar(any(ContasModel.class), any(ContaFactory.class))).thenReturn(contaSimulada);

        String requestBody = "{\"nomeDoUsuario\": \"Nome do Usuário\", \"valorAtualConta\": 1000.0}";

        mockMvc.perform(post("/api/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeDoUsuario").value("Nome do Usuário"))
                .andExpect(jsonPath("$.valorAtualConta").value(1000.0))
                .andExpect(jsonPath("$.valorFornecido").value(1000.0))
                .andExpect(jsonPath("$.tipoServico").value("Serviço Simulado"));
    }

    @Test
    public void testAtualizarConta() throws Exception {

        ContasModel contaAtualizada = new ContasModel();
        contaAtualizada.setId(1L);
        contaAtualizada.setNomeDoUsuario("NovoNome");
        contaAtualizada.setValorAtualConta(BigDecimal.valueOf(1500.0));

        when(contasService.alterar(eq(1L), any(ContasModel.class))).thenReturn(contaAtualizada);

        String requestBody = "{\"nomeDoUsuario\": \"NovoNome\", \"valorAtualConta\": 1500.0}";

        mockMvc.perform(put("/api/contas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeDoUsuario").value("NovoNome"))
                .andExpect(jsonPath("$.valorAtualConta").value(1500.0));
    }

    @Test
    public void testAlterarNomeDoUsuario() throws Exception {

        ContasModel contaSimulada = new ContasModel();
        contaSimulada.setId(1L);
        contaSimulada.setNomeDoUsuario("NomeAntigo");

        when(contasService.alterarNomeDoUsuario(eq(1L), anyString())).thenReturn(contaSimulada);

        mockMvc.perform(put("/api/contas/1/nome-usuario/Novo%20Usu%C3%A1rio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeDoUsuario").value("NomeAntigo"));
    }

    @Test
    public void testDeletarConta() throws Exception {

        mockMvc.perform(delete("/api/contas/1"))
                .andExpect(status().isOk());

        verify(contasService, times(1)).deletarConta(eq(1L));
    }

}

