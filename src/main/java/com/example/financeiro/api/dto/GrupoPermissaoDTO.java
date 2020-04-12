package com.example.financeiro.api.dto;

import java.util.List;

public class GrupoPermissaoDTO {
	private Long codigo;
	private String nome;
	private String descricao;
	private List<PermissaoDTO> permissoes;

	public GrupoPermissaoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<PermissaoDTO> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<PermissaoDTO> permissoes) {
		this.permissoes = permissoes;
	}

	public GrupoPermissaoDTO(Long codigo, String nome, String descricao, List<PermissaoDTO> permissoes) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
		this.permissoes = permissoes;
	}



}
