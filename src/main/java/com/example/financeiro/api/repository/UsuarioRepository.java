package com.example.financeiro.api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.repository.usuario.UsuarioRepositoryQuery;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryQuery {


	public Optional<Usuario> findByEmail(String email);
	
	public Optional<Usuario> findByEmailAndAtivoEquals(String email, boolean ativo);
	public Optional<Usuario> findByEmailAndDataNascimentoAndAtivoEquals(String email, LocalDate dataNascimento, boolean ativo);
	public List<Usuario> findByAtivoEquals(boolean ativo);
	// public List<Usuario> findByPermissoesDescricao(String permissaoDescricao);
	public Optional<Usuario> findByNome(String nome);


	
}
