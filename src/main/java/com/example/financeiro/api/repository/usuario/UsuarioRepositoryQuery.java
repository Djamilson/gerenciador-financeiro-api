package com.example.financeiro.api.repository.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.repository.filter.UsuarioFilter;

public interface UsuarioRepositoryQuery {
	public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable);
	public Optional<Usuario> buscaUsuarioPeloCodigo(Long codigo);
	
}
