package com.example.financeiro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	public Optional<Estado> findBySiglaContaining(String sigla);	

}
