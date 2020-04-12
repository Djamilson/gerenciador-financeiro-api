package com.example.financeiro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Imagem;;

public interface ImagemRepository extends JpaRepository<Imagem, Long> {

	List<Imagem> findByLancamentoCodigo(Long lancamentoCodigo);

}
