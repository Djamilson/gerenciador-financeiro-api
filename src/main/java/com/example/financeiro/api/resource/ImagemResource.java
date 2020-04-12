package com.example.financeiro.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.financeiro.api.dto.ImagemDTO;
import com.example.financeiro.api.model.Imagem;
import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.repository.ImagemRepository;
import com.example.financeiro.api.service.ImagemService;
import com.example.financeiro.api.storage.S3;

@RestController
@RequestMapping("/imagens")
public class ImagemResource {

	
	@Autowired
	private ImagemService imagemService;
	
	@Autowired
	private ImagemRepository imagemRepository;
	
	@Autowired
	private S3 s3;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")	
	public List<ImagemDTO> pesquisa(@RequestParam Long codigo) {
	System.out.println("===============================");
		return imagemService.listaImagemDTO(codigo);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		Optional<Imagem> imagem = imagemRepository.findById(codigo);
		// nome somente para fazer a busca no s3
		String nome = s3.retornaNomedoAnexo(imagem.get().getCaminho_s3()); 
		System.out.println("anexo:: " + nome);
		
		s3.remover(nome);
		
		imagemRepository.deleteById(imagem.get().getCodigo());
	}
}
