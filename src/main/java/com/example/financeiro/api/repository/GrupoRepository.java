package com.example.financeiro.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Usuario;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

	List<Grupo> findByUsuariosIn(Usuario usuario);
	public Page<Grupo> findByNomeContaining(String nome, Pageable pageable);	
	// public Page<Grupo> findByNomeAndDescricaoEquals(String nome, Pageable pageable, String descricao);
	public Grupo findByNome(String nome);
	
//	List<Grupo> findByUsuariosIn(Usuario usuario);
	
}