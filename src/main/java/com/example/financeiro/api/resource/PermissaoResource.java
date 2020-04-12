package com.example.financeiro.api.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.financeiro.api.dto.GrupoPermissaoDTO;
import com.example.financeiro.api.dto.PermissaoDTO;
import com.example.financeiro.api.event.RecursoCriadoEvent;
import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Permissao;
import com.example.financeiro.api.repository.GrupoRepository;
import com.example.financeiro.api.repository.PermissaoRepository;

@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {

	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Autowired
	private GrupoRepository grupoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERMISSAO_USUARIO') and #oauth2.hasScope('read')")
	public List<PermissaoDTO> listar() {
	List<PermissaoDTO> listaPermissaoDto = new ArrayList<>();
	
		permissaoRepository.findAll().forEach(permissao->{
		
			if(!permissao.getDescricao().equals("USUARIO_DEFAULT")) {
			
				PermissaoDTO p = new PermissaoDTO();
				p.setCodigo(permissao.getCodigo());
				p.setDescricao(permissao.getDescricao());
				
				listaPermissaoDto.add(p);	
			}
					
		});
		return listaPermissaoDto;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PERMISSAO') and #oauth2.hasScope('write')")
	public ResponseEntity<Permissao> criar(@Valid @RequestBody Permissao permissao, HttpServletResponse response) {
		Permissao permissaoSalva = permissaoRepository.save(permissao);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, permissaoSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(permissaoSalva);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERMISSAO_USUARIO') and #oauth2.hasScope('read')")
	public ResponseEntity<GrupoPermissaoDTO> buscarPermissaoPeloCodigoGrupo(@PathVariable Long codigo) {
		Optional<Grupo> grupo = grupoRepository.findById(codigo);
		
		GrupoPermissaoDTO grupoPermissao = new GrupoPermissaoDTO();
		grupoPermissao.setCodigo(grupo.get().getCodigo());
		grupoPermissao.setNome(grupo.get().getNome());
		grupoPermissao.setDescricao(grupo.get().getDescricao());
		
		List<PermissaoDTO> listaPermissoesDTO = new ArrayList<>();
		
		grupo.get().getPermissoes().forEach(permissao -> {
			PermissaoDTO permissaoDto = new PermissaoDTO();
			permissaoDto.setCodigo(permissao.getCodigo());
			permissaoDto.setDescricao(permissao.getDescricao());
			
			listaPermissoesDTO.add(permissaoDto);
		});
		
		grupoPermissao.setPermissoes(listaPermissoesDTO);
		
		
		 return grupo.isPresent() ? ResponseEntity.ok(grupoPermissao) : ResponseEntity.notFound().build();
	}
	
	
		
}
