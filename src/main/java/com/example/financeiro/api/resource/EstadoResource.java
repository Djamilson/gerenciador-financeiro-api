package com.example.financeiro.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.financeiro.api.model.Estado;
import com.example.financeiro.api.repository.EstadoRepository;

@RestController
@RequestMapping("/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}
	
	@GetMapping("/findBySigla")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Estado> buscarPelSigla(@RequestParam("sigla") String sigla) {
		
		System.out.println("Sigla: "+ sigla);
		Optional<Estado> estado = estadoRepository.findBySiglaContaining(sigla);
		
		System.out.println("Saida Sigla: "+ estado.get().getCodigo());
		 return ResponseEntity.noContent().build();
		//return estado.isPresent() ? ResponseEntity.ok(estado.get()) : ResponseEntity.notFound().build();
	}
}
