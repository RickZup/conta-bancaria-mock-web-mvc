package com.factory.contabancaria.controller;

import com.factory.contabancaria.dto.UsuarioRequestDTO;
import com.factory.contabancaria.dto.UsuarioResponseDTO;
import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.factory.ContaFactory;
import com.factory.contabancaria.repository.ContasRepository;
import com.factory.contabancaria.service.ContasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contas")
public class ContasController {

    @Autowired
    ContasService contasService;

    @Autowired
    ContasRepository contasRepository;

    //requisições
    //GET - Pegar as informações do nosso banco
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodasContas(){
        List<ContasModel> contasList = contasService.listarContas();

        List<UsuarioResponseDTO> responseList = new ArrayList<>();
        for (ContasModel conta : contasList) {
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setNumConta(conta.getNumConta());
            responseDTO.setAgencia(conta.getAgencia());
            responseDTO.setNomeDoUsuario(conta.getNomeDoUsuario());
            responseDTO.setValorAtualConta(conta.getValorAtualConta());
            responseList.add(responseDTO);
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> exibeUmaContaPeloId(@PathVariable Long id){
        Optional<ContasModel> contaOpcional = contasService.exibeContaPorId(id);
        if (contaOpcional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada, tente novamente!");
        }
        return ResponseEntity.ok(contaOpcional.get());
    }

    @GetMapping("/buscarPorNome/{nomeDoUsuario}")
    public ResponseEntity<UsuarioResponseDTO> exibirContaPorNomeDoUsuario(@PathVariable String nomeDoUsuario) {
        ContasModel contaEncontrada = contasService.exibirContaPorNomeDoUsuario(nomeDoUsuario);

        if (contaEncontrada == null) {
            return ResponseEntity.notFound().build();
        }

        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setNumConta(contaEncontrada.getNumConta());
        responseDTO.setAgencia(contaEncontrada.getAgencia());
        responseDTO.setNomeDoUsuario(contaEncontrada.getNomeDoUsuario());
        responseDTO.setValorAtualConta(contaEncontrada.getValorAtualConta());

        return ResponseEntity.ok(responseDTO);
    }

    //POST - Cria uma nova conta dentro do banco
    @PostMapping
    public ResponseEntity<UsuarioRequestDTO> cadastrarConta(@RequestBody ContasModel contasModel, ContaFactory contaFactory){
        ContasModel novaConta = contasService.cadastrar(contasModel, contaFactory);

        // Crie um novo objeto UsuarioRequestDTO com os atributos relevantes
        UsuarioRequestDTO usuarioDTO = new UsuarioRequestDTO();
        usuarioDTO.setNomeDoUsuario(novaConta.getNomeDoUsuario());
        usuarioDTO.setValorAtualConta(novaConta.getValorAtualConta());
        usuarioDTO.setValorFornecido(novaConta.getValorFornecido());
        usuarioDTO.setTipoServico(novaConta.getTipoServico());

        return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);
    }














    //PUT - Alterar uma conta já existente dentro do banco
    @PutMapping(path = "/{id}")
    public ContasModel atualizarConta(@PathVariable Long id, @RequestBody ContasModel contasModel){
        return contasService.alterar(id, contasModel);
    }



















    @PutMapping(path = "/{id}/nome-usuario/{novoNome}")
    public ResponseEntity<ContasModel> alterarNomeDoUsuario(
            @PathVariable Long id,
            @PathVariable String novoNome) throws ChangeSetPersister.NotFoundException {
        ContasModel contaAlterada = contasService.alterarNomeDoUsuario(id, novoNome);
        return ResponseEntity.ok(contaAlterada);
    }

    //DELETE - Deleta uma conta já existente dentro do banco
    @DeleteMapping(path = "/{id}")
    public void deletarConta(@PathVariable Long id){
        contasService.deletarConta(id);
    }

}
