package com.example.financeiro.api.dto;

public class PermissaoDTO {
	private Long codigo;
	private String descricao;
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public PermissaoDTO(Long codigo, String descricao) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
	}
	public PermissaoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
