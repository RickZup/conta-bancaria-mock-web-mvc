package com.factory.contabancaria.repository;

import com.factory.contabancaria.model.ContasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContasRepository extends JpaRepository<ContasModel, Long> {
    ContasModel findByNomeDoUsuario(String nomeDoUsuario);
}
