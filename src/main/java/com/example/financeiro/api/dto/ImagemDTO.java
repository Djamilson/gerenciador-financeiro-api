package com.example.financeiro.api.dto;

import java.time.LocalDateTime;

public class ImagemDTO {
	private Long codigo;
	private String nome;	
	private String caminho_s3;
	private LocalDateTime dataCad;
	private Long codigo_lancamento;
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
	public String getCaminho_s3() {
		return caminho_s3;
	}
	public void setCaminho_s3(String caminho_s3) {
		this.caminho_s3 = caminho_s3;
	}
	public LocalDateTime getDataCad() {
		return dataCad;
	}
	public void setDataCad(LocalDateTime dataCad) {
		this.dataCad = dataCad;
	}
	public Long getCodigo_lancamento() {
		return codigo_lancamento;
	}
	public void setCodigo_lancamento(Long codigo_lancamento) {
		this.codigo_lancamento = codigo_lancamento;
	}
	public ImagemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImagemDTO(Long codigo, String nome, String caminho_s3, LocalDateTime dataCad, Long codigo_lancamento) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.caminho_s3 = caminho_s3;
		this.dataCad = dataCad;
		this.codigo_lancamento = codigo_lancamento;
	} 
	
		
}
