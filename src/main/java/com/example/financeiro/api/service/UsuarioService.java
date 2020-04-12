package com.example.financeiro.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.financeiro.api.mail.Mailer;
import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Permissao;
import com.example.financeiro.api.model.Pessoa;
import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.repository.GrupoRepository;
import com.example.financeiro.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private GrupoRepository grupoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	public Optional<Usuario> avisarRecuperarSenha(Usuario usuario, String token) {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de " + "e-mails de recuperação de senha.");
		}

		mailer.recuperarSenha(usuario, token);

		logger.info("Envio de e-mail de aviso concluído.");

		return null;
	}

	public Usuario salvar(Usuario usuario) {

		Usuario usuarioParaSalva = new Usuario();
		List<Grupo> grups = new ArrayList<Grupo>();
		List<Permissao> permis = new ArrayList<Permissao>();

		System.out.println("Total de Grupso: "+usuario.getGrupos().size());
		usuarioParaSalva.setCodigo(usuario.getCodigo());
		usuarioParaSalva.setNome(usuario.getNome());
		usuarioParaSalva.setSobreNome(usuario.getSobreNome());
		usuarioParaSalva.setEmail(usuario.getEmail());
		usuarioParaSalva.setDataNascimento(usuario.getDataNascimento());
		usuarioParaSalva.setAtivo(usuario.isAtivo());
		usuarioParaSalva.setSenha(usuario.getSenha());

		usuario.getGrupos().forEach((g) -> {
			System.out.println(g.getCodigo());
			System.out.println(g.getNome());

			grups.add(grupoRepository.findById(g.getCodigo()).get());
		});

		grups.forEach((gru) -> {
			gru.getPermissoes().forEach(p -> {

				if (permis.contains("")) {
					permis.add(p);
				}

			});

		});

		usuarioParaSalva.setGrupos(grups);

		System.out.println("Grupos total:: " + usuarioParaSalva.getGrupos().size());
		// usuarioParaSalva.setPermissoes(permis);

		return usuarioRepository.save(usuarioParaSalva);
	}

	private String criptografarSenha(Usuario usuario) {
		// cripografando senha

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(usuario.getSenha());
	}

	
	public Usuario atualizar(Long codigo, Usuario usuario) {
		Usuario usuarioSalvo = buscarUsuarioPeloCodigo(codigo);	
		
		Usuario usuarioParaSalva = new Usuario();
		List<Grupo> grups = new ArrayList<Grupo>();
		List<Permissao> permis = new ArrayList<Permissao>();

		usuarioSalvo.getGrupos().clear();

		usuarioParaSalva.setCodigo(usuario.getCodigo());
		usuarioParaSalva.setNome(usuario.getNome());
		usuarioParaSalva.setSobreNome(usuario.getSobreNome());
		usuarioParaSalva.setEmail(usuario.getEmail());
		usuarioParaSalva.setDataNascimento(usuario.getDataNascimento());
		usuarioParaSalva.setAtivo(usuario.isAtivo());
		usuarioParaSalva.setSenha(usuario.getSenha());

		usuario.getGrupos().forEach((g) -> {
			grups.add(grupoRepository.findById(g.getCodigo()).get());
		});

		grups.forEach((gru) -> {
			gru.getPermissoes().forEach(p -> {
				if (permis.contains("")) {
					permis.add(p);
				}

			});

		});


		usuarioSalvo.getGrupos().addAll(grups);
		usuarioParaSalva.setGrupos(grups);
						
		BeanUtils.copyProperties(usuarioParaSalva, usuarioSalvo, "codigo", "grupos");
		return usuarioRepository.save(usuarioSalvo);
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Usuario usuarioSalva = buscarUsuarioPeloCodigo(codigo);
		usuarioSalva.setAtivo(ativo);
		usuarioRepository.save(usuarioSalva);
	}
	
	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	public Usuario buscarUsuarioPeloCodigo(Long codigo) {
		Optional<Usuario> usuarioSalva = usuarioRepository.findById(codigo);
		if (!usuarioSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return usuarioSalva.get();
	}

}
