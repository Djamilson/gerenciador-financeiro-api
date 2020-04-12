package com.example.financeiro.api.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.financeiro.api.model.TipoLancamento;

public class ResumoLancamento_ {

	private Long codigo;
	private String descricao;
	
	private LocalDate dataVencimento;
	private LocalDate dataPagamento;
	private LocalDateTime dataCad;
	private LocalDateTime dataBaixa;
	private LocalDateTime dataUpdate;
	
	private BigDecimal valor;
	private BigDecimal valorPagoRecebido;
	private TipoLancamento tipo;
	private String categoria;
	private String pessoa;	
	private String numeroLancamento;	
	
	public ResumoLancamento_(Long codigo, String descricao, LocalDate dataVencimento, LocalDate dataPagamento,
			LocalDateTime dataCad, LocalDateTime dataBaixa, LocalDateTime dataUpdate, BigDecimal valor,
			BigDecimal valorPagoRecebido, TipoLancamento tipo, String categoria, String pessoa, String numeroLancamento) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.dataCad = dataCad;
		this.dataBaixa = dataBaixa;
		this.dataUpdate = dataUpdate;
		this.valor = valor;
		this.valorPagoRecebido = valorPagoRecebido;
		this.tipo = tipo;
		this.categoria = categoria;
		this.pessoa = pessoa;
		this.numeroLancamento = numeroLancamento;
	}

	public LocalDateTime getDataCad() {
		return dataCad;
	}

	public void setDataCad(LocalDateTime dataCad) {
		this.dataCad = dataCad;
	}

	public LocalDateTime getDataBaixa() {
		return dataBaixa;
	}

	public String getNumeroLancamento() {
		return numeroLancamento;
	}

	public void setNumeroLancamento(String numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}

	public void setDataBaixa(LocalDateTime dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public LocalDateTime getDataUpdate() {
		return dataUpdate;
	}

	public void setDataUpdate(LocalDateTime dataUpdate) {
		this.dataUpdate = dataUpdate;
	}
	
	public BigDecimal getValorPagoRecebido() {
		return valorPagoRecebido;
	}

	public void setValorPagoRecebido(BigDecimal valorPagoRecebido) {
		this.valorPagoRecebido = valorPagoRecebido;
	}

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

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getPessoa() {
		return pessoa;
	}

	public void setPessoa(String pessoa) {
		this.pessoa = pessoa;
	}


	

}
