package com.factory.contabancaria.service;

import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.factory.ContaFactory;
import com.factory.contabancaria.repository.ContasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ContasService {
    @Autowired
    ContasRepository contasRepository;

    //métodos
    public List<ContasModel> listarContas(){
        return contasRepository.findAll();
    }

    public Optional<ContasModel> exibeContaPorId(Long id){
        return contasRepository.findById(id);
    }

    public ContasModel exibirContaPorNomeDoUsuario(String nomeDoUsuario) {
        return contasRepository.findByNomeDoUsuario(nomeDoUsuario);
    }

    public ContasModel cadastrar(ContasModel contasModel, ContaFactory contaFactory){
        BigDecimal resultado = contaFactory.tipoServicoConta(contasModel.getTipoServico())
                .calcular(contasModel.getValorAtualConta(), contasModel.getValorFornecido());
        contasModel.setValorFinal(resultado);

        if ("DEPOSITO".equals(contasModel.getTipoServico())) {
            contasModel.setValorAtualConta(contasModel.getValorAtualConta().add(contasModel.getValorFornecido()));
        } else if ("SAQUE".equals(contasModel.getTipoServico())) {
            contasModel.setValorAtualConta(contasModel.getValorAtualConta().subtract(contasModel.getValorFornecido()));
        }
        return contasRepository.save(contasModel);
    }

//                        ----- MÉTODO DA JOY -----

//    public ContasModel cadastrar(ContasModel contasModel, ContaFactory contaFactory){
//        BigDecimal resultado = contaFactory.tipoServicoConta(contasModel.getTipoServico())
//                .calcular(contasModel.getValorAtualConta(), contasModel.getValorFornecido());
//        contasModel.setValorFinal(resultado);
//        return contasRepository.save(contasModel);
//    }

    public ContasModel alterar(Long id, ContasModel contasModel) {

        ContasModel conta = exibeContaPorId(id).get();

        if (contasModel.getNumConta() != null) {
            conta.setNumConta(contasModel.getNumConta());
        }
        if (contasModel.getAgencia() != null) {
            conta.setAgencia(contasModel.getAgencia());
        }

        return contasRepository.save(conta);
    }

    public ContasModel alterarNomeDoUsuario(Long id, String novoNome) throws ChangeSetPersister.NotFoundException {
        Optional<ContasModel> contaOptional = contasRepository.findById(id);

        if (contaOptional.isPresent()) {
            ContasModel conta = contaOptional.get();
            conta.setNomeDoUsuario(novoNome);
            return contasRepository.save(conta);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public void deletarConta(Long id){
        contasRepository.deleteById(id);
    }

}
