package com.example.financeiro.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	public Page<Categoria> findByNomeContaining(String nome, Pageable pageable);
	public Optional<Categoria> findByNomeContaining(String nome);	

}
