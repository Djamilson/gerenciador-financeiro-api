package com.example.financeiro.api.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.financeiro.api.dto.GrupoDTO;
import com.example.financeiro.api.event.RecursoCriadoEvent;
import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.repository.GrupoRepository;
import com.example.financeiro.api.service.GrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoResource {

	@Autowired
	private GrupoRepository grupoRepository;

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	public static final String GRUPO_DESCRICAO = "Default";


	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
	public ResponseEntity<Grupo> criar(@Valid @RequestBody Grupo grupo, HttpServletResponse response) {
		Grupo grupoSalvo = grupoService.salvar(grupo);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, grupoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(grupoSalvo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
	public ResponseEntity<Grupo> atualizar(@PathVariable Long codigo, @Valid @RequestBody Grupo grupo) {
		Grupo grupoSalva = grupoService.atualizar(codigo, grupo);
		return ResponseEntity.ok(grupoSalva);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_USUARIO') and #oauth2.hasScope('read')")
	public ResponseEntity<GrupoDTO> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Grupo> grupo = grupoRepository.findById(codigo);
		System.out.println(">>>>>>   " + grupo.get().getNome());
		GrupoDTO grupoDto = new GrupoDTO();
		grupoDto.setCodigo(grupo.get().getCodigo());
		grupoDto.setNome(grupo.get().getNome());
		grupoDto.setDescricao(grupo.get().getDescricao());
		
		return grupo.isPresent() ? ResponseEntity.ok(grupoDto) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_USUARIO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		grupoRepository.deleteById(codigo);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO')")
	public Page<Grupo> pesquisar(@RequestParam(required = false, defaultValue = "%") String nome, Pageable pageable) {
		
		return grupoRepository.findByNomeContaining(nome, pageable);
	}

	@GetMapping("/todos")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO')")
	public List<GrupoDTO> listar() {

		List<GrupoDTO> novaLista = new ArrayList<GrupoDTO>();

		grupoRepository.findAll().forEach(g -> {
			GrupoDTO grupo = new GrupoDTO();
			
			if (!(g.getDescricao().equals(GRUPO_DESCRICAO))) {
					
				grupo.setCodigo(g.getCodigo());
				grupo.setNome(g.getNome());
				grupo.setDescricao(g.getDescricao());
				novaLista.add(grupo);
					
			}
		});

		return novaLista;
	}
}
