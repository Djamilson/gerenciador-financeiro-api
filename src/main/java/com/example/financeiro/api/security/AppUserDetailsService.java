package com.example.financeiro.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.repository.GrupoRepository;
import com.example.financeiro.api.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private GrupoRepository grupoRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailAndAtivoEquals(email, true);

		Usuario usuario = usuarioOptional
				.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));

		return new UsuarioSistema(usuario, authorities(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(Usuario usuario) {
		return authorities(grupoRepository.findByUsuariosIn(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(List<Grupo> grupos) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		grupos.forEach(g -> g.getPermissoes()
				.forEach(
						(p) -> {
							authorities.add(new SimpleGrantedAuthority("ROLE_" + p.getDescricao().toUpperCase()));
														
						}));

		return authorities;
	}

}
