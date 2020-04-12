package com.example.financeiro.api.resource;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.financeiro.api.dto.ImagemDTO;
import com.example.financeiro.api.dto.LancamentoEstatisticaCategoria;
import com.example.financeiro.api.dto.LancamentoEstatisticaDia;
import com.example.financeiro.api.dto.LancamentoTotal;
import com.example.financeiro.api.event.RecursoCriadoEvent;
import com.example.financeiro.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.example.financeiro.api.model.Imagem;
import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.model.TipoLancamento;
import com.example.financeiro.api.repository.ImagemRepository;
import com.example.financeiro.api.repository.LancamentoRepository;
import com.example.financeiro.api.repository.filter.LancamentoFilter;
import com.example.financeiro.api.repository.projection.ResumoLancamento;
import com.example.financeiro.api.service.ImagemService;
import com.example.financeiro.api.service.LancamentoService;
import com.example.financeiro.api.service.exception.PessoaInexistenteOuInativaException;
import com.example.financeiro.api.storage.S3;
import com.example.financeiro.api.repository.projection.ResumoLancamento_;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private ImagemService imagemService;

	@Autowired
	private ImagemRepository imagemRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private S3 s3;

	@PostMapping("/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public List<ImagemDTO> uploadAnexo(@RequestParam MultipartFile[] anexo, @RequestParam Long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		imagemService.anexarOsDados(anexo, lancamento.get());

		return imagemService.listaImagemDTO(lancamento.get().getCodigo());
	}

	@GetMapping("/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<byte[]> downloadFile(@RequestParam Long anexo) {
		Optional<Imagem> imagem = imagemRepository.findById(anexo);
		System.out.println("anexo:: " + anexo);
		// nome somente para fazer a busca no s3
		String nome = s3.retornaNomedoAnexo(imagem.get().getCaminho_s3()); 

		ByteArrayOutputStream downloadInputStream = s3.downloadFile(nome);
		System.out.println("===>> downloadInputStream: " + downloadInputStream.size());

		return ResponseEntity.ok().contentType(contentType(imagem.get().getNome()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imagem.get().getNome() + "\"")
				.body(downloadInputStream.toByteArray());
	}

	private MediaType contentType(String keyname) {
		String[] arr = keyname.split("\\.");
		String type = arr[arr.length - 1];
		switch (type) {
		case "txt":
			return MediaType.TEXT_PLAIN;
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		case "gif":
			return MediaType.IMAGE_GIF;

		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	@GetMapping("/relatorios/por-pessoa")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<byte[]> relatorioPorPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {
		byte[] relatorio = lancamentoService.relatorioPorPessoa(inicio, fim);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(relatorio);
	}

	@GetMapping("/estatisticas/por-dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaDia> porDia() {
		return this.lancamentoRepository.porDia(LocalDate.now());
	}

	@GetMapping("/estatisticas/por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaCategoria> porCategoria() {
		return this.lancamentoRepository.porCategoria(LocalDate.now());
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento_> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	@GetMapping("/totalReceitasMes")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public BigDecimal totalReceitaMes(LancamentoFilter lancamentoFilter) {

		TipoLancamento tipoLancamento = TipoLancamento.RECEITA;
		lancamentoFilter.setTipoLancamento(tipoLancamento);

		List<ResumoLancamento> list = lancamentoRepository.totalLancamentosMes(lancamentoFilter);

		BigDecimal sum = list.stream().map(ResumoLancamento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("Total de receita do Mês::: " + sum);

		return sum;
	}

	@GetMapping("/totalDespesasMes")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public BigDecimal totalDespesasMes(LancamentoFilter lancamentoFilter) {

		TipoLancamento tipoLancamento_despesas = TipoLancamento.DESPESA;
		lancamentoFilter.setTipoLancamento(tipoLancamento_despesas);

		List<ResumoLancamento> listDespesas = lancamentoRepository.totalLancamentosMes(lancamentoFilter);

		BigDecimal sum = listDespesas.stream().map(ResumoLancamento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println(sum);

		return sum;

	}

	@GetMapping("/totalReceitasMenosDespesasMes")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<LancamentoTotal> totalReceitasMenosDespesasMes() {

		LancamentoFilter lancamentoFilterReceita = new LancamentoFilter();
		TipoLancamento tipoLancamentoReceita = TipoLancamento.RECEITA;
		lancamentoFilterReceita.setTipoLancamento(tipoLancamentoReceita);

		LancamentoFilter lancamentoFilterDespesa = new LancamentoFilter();
		TipoLancamento tipoLancamentoDespesa = TipoLancamento.DESPESA;
		lancamentoFilterDespesa.setTipoLancamento(tipoLancamentoDespesa);

		List<ResumoLancamento> listReceitas = lancamentoRepository.totalLancamentosMes(lancamentoFilterReceita);
		List<ResumoLancamento> listDespesas = lancamentoRepository.totalLancamentosMes(lancamentoFilterDespesa);

		BigDecimal totalReceitasMes = listReceitas.stream().map(ResumoLancamento::getValor).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		BigDecimal totalDespesasMes = listDespesas.stream().map(ResumoLancamento::getValor).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		LancamentoTotal lancamentoTotal = new LancamentoTotal();
		lancamentoTotal.setTotalReceitasMes(totalReceitasMes);
		lancamentoTotal.setTotalDespesasMes(totalDespesasMes);
		lancamentoTotal.setTotalReceitasDespesasMes(totalReceitasMes.subtract(totalDespesasMes));

		return ResponseEntity.ok(lancamentoTotal);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		try {
			Lancamento lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
			return ResponseEntity.ok(lancamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
