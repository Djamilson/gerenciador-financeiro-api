package com.example.financeiro.api.resource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import com.example.financeiro.api.dto.UsuarioDTO;
import com.example.financeiro.api.dto.VerificationTokenDTO;
import com.example.financeiro.api.event.RecursoCriadoEvent;
import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.model.VerificationToken;
import com.example.financeiro.api.repository.UsuarioRepository;
import com.example.financeiro.api.repository.VerificationTokenRepository;
import com.example.financeiro.api.repository.filter.UsuarioFilter;
import com.example.financeiro.api.service.TokenRecuperarSenhaService;
import com.example.financeiro.api.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private TokenRecuperarSenhaService tokenRecuperaSenhaService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ApplicationEventPublisher publisher;

	public static final String USUARIO_DEFAULT = "Usuario Default";

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
	public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario, HttpServletResponse response) {

		Usuario usuarioSalvo = usuarioService.salvar(usuario);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
	}

	@GetMapping("/recuperar-senha")
	@PreAuthorize("hasAuthority('ROLE_USUARIO_DEFAULT') and #oauth2.hasScope('read')")
	public ResponseEntity<Usuario> recuperaSenha(@RequestParam String usuario, @RequestParam String data) {

		LocalDate data_ = LocalDate.parse(data);

		Optional<Usuario> usuarioBusca = usuarioRepository.findByEmailAndDataNascimentoAndAtivoEquals(usuario, data_,
				true);

		final String token = UUID.randomUUID().toString();

		tokenRecuperaSenhaService.createVerificationTokenForUser(usuarioBusca.get(), token);

		usuarioService.avisarRecuperarSenha(usuarioBusca.get(), token);

		return usuarioBusca.isPresent() ? ResponseEntity.ok(usuarioBusca.get()) : ResponseEntity.notFound().build();
	}

	@GetMapping("/verificaTokenValido")
	@PreAuthorize("hasAuthority('ROLE_USUARIO_DEFAULT') and #oauth2.hasScope('read')")
	public ResponseEntity<VerificationTokenDTO> verificaTokenValido(@RequestParam String token) {

		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);

		String verificationTokenBusca = tokenRecuperaSenhaService.validateVerificationToken(verificationToken.get());

		System.out.println("verificationTokenBusca >> " + verificationTokenBusca);

		if (verificationTokenBusca == "TOKEN_EXPIRED") {
			verificationToken = null;
		}

		VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO();

		verificationTokenDTO.setCodigo(verificationToken.get().getCodigo());
		verificationTokenDTO.setCodigo_usuario(verificationToken.get().getUser().getCodigo());
		verificationTokenDTO.setExpiryDate(verificationToken.get().getExpiryDate());
		verificationTokenDTO.setToken(verificationToken.get().getToken());

		return verificationToken.isPresent() ? ResponseEntity.ok(verificationTokenDTO)
				: ResponseEntity.notFound().build();

	}

	@GetMapping("/verificarEmail")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('read')")
	public ResponseEntity<String> verificarEmail(@RequestParam String email) {

		System.out.println("Email: " + email);

		Optional<Usuario> usuarioEmail = usuarioService.findByEmail(email);

		HttpHeaders responseHeaders = new HttpHeaders();

		responseHeaders.add("Content-Type", "application/json");

		return usuarioEmail.isPresent() ? ResponseEntity.notFound().build() : null;

	}

	@PutMapping("/atualizandoUsuario")
	@PreAuthorize("hasAuthority('ROLE_USUARIO_DEFAULT') and #oauth2.hasScope('write')")
	public void atualizar(@RequestParam Long codigo_usuario, @RequestParam String senha) {
		Optional<Usuario> usuario = usuarioRepository.findById(codigo_usuario);

		Usuario usuarioParaSalva = new Usuario();
		usuarioParaSalva = usuario.get();
		usuarioParaSalva.setSenha(senha);

		System.out.println(usuario.get().getSenha());
		usuarioService.atualizar(codigo_usuario, usuarioParaSalva);

	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long codigo, @Valid @RequestBody Usuario usuario) {
		Usuario usuarioSalva = usuarioService.atualizar(codigo, usuario);
		return ResponseEntity.ok(usuarioSalva);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_USUARIO') and #oauth2.hasScope('read')")
	public ResponseEntity<UsuarioDTO> buscarPeloCodigo(@PathVariable Long codigo) {

		Optional<Usuario> usuario = usuarioRepository.findById(codigo);
		UsuarioDTO usuarioDto = new UsuarioDTO();

		if (usuario.get().getNome().equals(USUARIO_DEFAULT)) {
			usuario = null;
		}

		usuarioDto.setCodigo(usuario.get().getCodigo());
		usuarioDto.setNome(usuario.get().getNome());
		usuarioDto.setSobreNome(usuario.get().getSobreNome());
		usuarioDto.setDataNascimento(usuario.get().getDataNascimento());
		usuarioDto.setSenha(usuario.get().getSenha());
		usuarioDto.setAtivo(usuario.get().isAtivo());
		usuarioDto.setEmail(usuario.get().getEmail());

		List<GrupoDTO> listaDeGrupoDTO = new ArrayList<>();
		Iterator<Grupo> iter = usuario.get().getGrupos().iterator();

		while (iter.hasNext()) {
			Grupo grupo = iter.next();

			GrupoDTO grupoDTOO = new GrupoDTO();
			grupoDTOO.setCodigo(grupo.getCodigo());
			grupoDTOO.setNome(grupo.getNome());
			grupoDTOO.setDescricao(grupo.getDescricao());

			listaDeGrupoDTO.add(grupoDTOO);

		}

		System.out.println("Quantidade de grupos final:: " + listaDeGrupoDTO.size());

		usuarioDto.setGrupos(removeElementosRepetidos(listaDeGrupoDTO));

		return usuario.isPresent() ? ResponseEntity.ok(usuarioDto) : ResponseEntity.notFound().build();
	}

	private List<GrupoDTO> removeElementosRepetidos(List<GrupoDTO> filtroGrupoList) {
		List<GrupoDTO> novaLista = new ArrayList<GrupoDTO>();
		for (int i = 0; i < filtroGrupoList.size(); i++) {
			if (novaLista.isEmpty()) {
				novaLista.add(filtroGrupoList.get(i));
			} else {
				int count = 0;
				for (GrupoDTO f : novaLista) {
					if (filtroGrupoList.get(i).getNome().equalsIgnoreCase(f.getNome())) {
						count++;
					}
				}
				if (count == 0) {
					novaLista.add(filtroGrupoList.get(i));
				}
			}
		}
		return novaLista;
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_USUARIO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		usuarioRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		usuarioService.atualizarPropriedadeAtivo(codigo, ativo);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_USUARIO')")
	public Page<Usuario> pesquisar(UsuarioFilter usuarioFilter, Pageable pageable) {
		usuarioFilter.setUsuario_default(USUARIO_DEFAULT);

		return usuarioRepository.filtrar(usuarioFilter, pageable);
	}

}
