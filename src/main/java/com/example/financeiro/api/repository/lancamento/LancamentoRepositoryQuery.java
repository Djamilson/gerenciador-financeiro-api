package com.example.financeiro.api.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.financeiro.api.dto.LancamentoEstatisticaCategoria;
import com.example.financeiro.api.dto.LancamentoEstatisticaDia;
import com.example.financeiro.api.dto.LancamentoEstatisticaPessoa;
import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.repository.filter.LancamentoFilter;
import com.example.financeiro.api.repository.projection.ResumoLancamento;
import com.example.financeiro.api.repository.projection.ResumoLancamento_;

public interface LancamentoRepositoryQuery {

	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);

	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);

	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public Page<ResumoLancamento_> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public List<ResumoLancamento> totalLancamentosMes(LancamentoFilter lancamentoFilter);

}
