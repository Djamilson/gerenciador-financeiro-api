package com.example.financeiro.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
	List<Permissao> findByGruposIn(Grupo grupo);
	public Optional<Permissao> findByDescricao(String descricao);
	
}
