package com.example.financeiro.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.financeiro.api.dto.LancamentoEstatisticaPessoa;
import com.example.financeiro.api.mail.Mailer;
import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.model.Permissao;
import com.example.financeiro.api.model.Pessoa;
import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.repository.LancamentoRepository;
import com.example.financeiro.api.repository.PermissaoRepository;
import com.example.financeiro.api.repository.PessoaRepository;
import com.example.financeiro.api.service.exception.PessoaInexistenteOuInativaException;
import com.example.financeiro.api.storage.S3;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {

	private static final String DESTINATARIOS = "PESQUISAR_LANCAMENTO";

	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PermissaoRepository permissaoRepository;

	@Autowired
	private Mailer mailer;

	@Autowired
	private S3 s3;

	@Scheduled(cron = "0 0 6 * * *")
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de " + "e-mails de aviso de lançamentos vencidos.");
		}

		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

		if (vencidos.isEmpty()) {
			logger.info("Sem lançamentos vencidos para aviso.");

			return;
		}

		logger.info("Exitem {} lançamentos vencidos.", vencidos.size());

		Optional<Permissao> permissao = permissaoRepository.findByDescricao(DESTINATARIOS);

		List<Grupo> listaGrupos = permissao.get().getGrupos();

		List<Usuario> destinatarios = new ArrayList<>();
		listaGrupos.forEach(grupos -> {
			destinatarios.addAll(grupos.getUsuarios());
		});

		if (destinatarios.isEmpty()) {
			logger.warn("Existem lançamentos vencidos, mas o " + "sistema não encontrou destinatários.");

			return;
		}

		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

		logger.info("Envio de e-mail de aviso concluído.");
	}

	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public Lancamento salvar(Lancamento lancamento) {
		validarPessoa(lancamento);

		if (StringUtils.hasText(lancamento.getAnexo())) {
			s3.salvar(lancamento.getAnexo());
		}

		return salvaLoop(lancamento);
	}

	private Lancamento salvaLoop(Lancamento lancamento) {

		Lancamento lancamentoRetorno = new Lancamento();
		System.out.println("Número do lancamento: "+ lancamento.getNumeroLancamento());
		/*
		Stream<Integer> numbers = Stream.iterate(0, n -> n + 1 );		
		numbers.limit(Integer.parseInt(lancamento.getNumeroLancamento())).forEach(n -> {
		
			Lancamento l = new Lancamento();
			Integer numeroLancamento = n + 1;
			String barra = "/".concat(lancamento.getNumeroLancamento());
			
			l.setNumeroLancamento(numeroLancamento.toString().concat(barra));
			l.setCategoria(lancamento.getCategoria());
			l.setDataCad(lancamento.getDataCad());
			l.setDescricao(lancamento.getDescricao());
			l.setObservacao(lancamento.getObservacao());
			l.setPessoa(lancamento.getPessoa());
			l.setTipo(lancamento.getTipo());
			l.setValor(lancamento.getValor());
			
	
			if (numeroLancamento < 2) {
				 l.setDataVencimento(lancamento.getDataVencimento());				
				 lancamentoRepository.save(l);
			} else {
				System.out.println("Começando a incrementar a data: "+n);
				l.setDataVencimento(lancamento.getDataVencimento().plusMonths(n));
				lancamentoRepository.save(l);
			}

		});*/
		

		for (Integer i = 1; i <= Integer.parseInt(lancamento.getNumeroLancamento()); i++) {
			Lancamento l = new Lancamento();
			Integer numeroLancamento = i;
			String barra = "/".concat(lancamento.getNumeroLancamento());
			
			l.setNumeroLancamento(numeroLancamento.toString().concat(barra));
			l.setCategoria(lancamento.getCategoria());
			l.setDataCad(lancamento.getDataCad());
			l.setDescricao(lancamento.getDescricao());
			l.setObservacao(lancamento.getObservacao());
			l.setPessoa(lancamento.getPessoa());
			l.setTipo(lancamento.getTipo());
			l.setValor(lancamento.getValor());
			
	
			if (numeroLancamento < 2) {
				 l.setDataVencimento(lancamento.getDataVencimento());				
				lancamentoRetorno = lancamentoRepository.save(l);
			} else {
				System.out.println("Começando a incrementar a data: "+i);
				l.setDataVencimento(lancamento.getDataVencimento().plusMonths(i));
				lancamentoRepository.save(l);
			}

		} 

		return lancamentoRetorno;
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}

		if (StringUtils.isEmpty(lancamento.getAnexo()) && StringUtils.hasText(lancamentoSalvo.getAnexo())) {
			s3.remover(lancamentoSalvo.getAnexo());
		} else if (StringUtils.hasText(lancamento.getAnexo())
				&& !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = pessoaRepository.getOne(lancamento.getPessoa().getCodigo());
		}

		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
		if (!lancamentoSalvo.isPresent()) {
			throw new IllegalArgumentException();
		}
		return lancamentoSalvo.get();
	}

}
