package com.example.financeiro.api.dto;

import java.math.BigDecimal;

public class LancamentoTotal {

	private BigDecimal totalReceitasMes;
	private BigDecimal totalDespesasMes;
	private BigDecimal totalReceitasDespesasMes;

	
	public LancamentoTotal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BigDecimal getTotalReceitasMes() {
		return totalReceitasMes;
	}

	public void setTotalReceitasMes(BigDecimal totalReceitasMes) {
		this.totalReceitasMes = totalReceitasMes;
	}

	public BigDecimal getTotalDespesasMes() {
		return totalDespesasMes;
	}

	public void setTotalDespesasMes(BigDecimal totalDespesasMes) {
		this.totalDespesasMes = totalDespesasMes;
	}

	public BigDecimal getTotalReceitasDespesasMes() {
		return totalReceitasDespesasMes;
	}

	public void setTotalReceitasDespesasMes(BigDecimal totalReceitasDespesasMes) {
		this.totalReceitasDespesasMes = totalReceitasDespesasMes;
	}

}
