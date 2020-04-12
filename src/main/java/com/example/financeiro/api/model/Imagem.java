package com.example.financeiro.api.model;

import java.io.Serializable;
import java.text.Normalizer;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "imagem")
public class Imagem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@Column(name = "nome")
	private String nome;

	@Column(name = "caminho_s3")
	private String caminho_s3;

	@Column(name = "data_cad")
	private LocalDateTime dataCad;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_lancamento")
	private Lancamento lancamento; 
		
	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataCad() {
		return dataCad;
	}

	public void setDataCad(LocalDateTime dataCad) {
		this.dataCad = dataCad;
	}

	public String getCaminho_s3() {
		return caminho_s3;
	}

	public void setCaminho_s3(String caminho_s3) {
		this.caminho_s3 = caminho_s3;
	}

	public Imagem(String nome, String caminho_s3) {
		super();
		this.nome = nome;
		this.caminho_s3 = caminho_s3;	
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Imagem() {
		super();
		// TODO Auto-generated constructor stub
	}


	// trata o nome da imagem
	@Transient
	public String getPegaNomeArquivo(String nomeArquivo) {

		String nome = (nomeArquivo.substring(nomeArquivo.lastIndexOf("\\")+1));
		String resul = nome.replaceAll("." + nome.substring(nome.lastIndexOf('.') + 1), "");

		CharSequence cs = new StringBuilder(resul.replace(" ", ""));

		return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Imagem other = (Imagem) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	
	}
