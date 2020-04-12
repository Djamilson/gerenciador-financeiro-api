package com.example.financeiro.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.financeiro.api.model.Cidade;
import com.example.financeiro.api.model.Estado;
import com.example.financeiro.api.repository.CidadeRepository;
import com.example.financeiro.api.repository.EstadoRepository;

@RestController
@RequestMapping("/cidades")
public class CidadeResource {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public List<Cidade> pesquisar(@RequestParam Long estado) {
		return cidadeRepository.findByEstadoCodigo(estado);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/findBySigla")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public List<Cidade> pesquisarCidadeSiglaUF(@RequestParam("sigla") String sigla) {

		Optional<Estado> estado = estadoRepository.findBySiglaContaining(sigla);

		return estado.isPresent() ? cidadeRepository.findByEstadoCodigo(estado.get().getCodigo())
				: (List<Cidade>) ResponseEntity.notFound().build();

	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Cidade> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Cidade> cidade = cidadeRepository.findById(codigo);
		return cidade.isPresent() ? ResponseEntity.ok(cidade.get()) : ResponseEntity.notFound().build();
	}

}
