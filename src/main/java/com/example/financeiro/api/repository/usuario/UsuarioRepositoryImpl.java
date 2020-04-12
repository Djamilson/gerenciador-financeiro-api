package com.example.financeiro.api.repository.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.financeiro.api.dto.UsuarioDTO;
import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.model.Usuario_;
import com.example.financeiro.api.repository.UsuarioRepository;
import com.example.financeiro.api.repository.filter.UsuarioFilter;

public class UsuarioRepositoryImpl implements UsuarioRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);

		Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Usuario> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));
	}

	private Predicate[] criarRestricoes(UsuarioFilter usuarioFilter, CriteriaBuilder builder, Root<Usuario> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(usuarioFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get(Usuario_.nome)),
					"%" + usuarioFilter.getNome().toLowerCase() + "%"));
		}

		if (!StringUtils.isEmpty(usuarioFilter.getUsuario_default())) {
			predicates.add(builder.notEqual(builder.lower(root.get(Usuario_.nome)),
					usuarioFilter.getUsuario_default().toLowerCase()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(UsuarioFilter usuarioFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Usuario> root = criteria.from(Usuario.class);

		Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

	@Override
	public Optional<Usuario> buscaUsuarioPeloCodigo(Long codigo) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(codigo);
		 UsuarioDTO usuarioDto = new UsuarioDTO();
					
		usuarioDto.setCodigo(usuario.get().getCodigo());
		usuarioDto.setNome(usuario.get().getNome());
		usuarioDto.setDataNascimento(usuario.get().getDataNascimento());
		usuarioDto.setSenha(usuario.get().getSenha());
		usuarioDto.setAtivo(usuario.get().isAtivo());

		List<Grupo> listaDeGrupoDTO = new ArrayList<>();
		
		usuario.get().getGrupos().forEach(grupo -> {
			System.out.println("Grupo"+grupo.getDescricao());
			System.out.println("Grupo"+grupo.getNome());
			System.out.println("Grupo"+grupo.getCodigo());
			
			//Grupo grupo = new Grupo();
			grupo.setCodigo(grupo.getCodigo());
			grupo.setNome(grupo.getNome());
			grupo.setDescricao(grupo.getDescricao());
			listaDeGrupoDTO.add(grupo);			
		});
		
		usuario.get().getGrupos().clear();
		// usuario.get().getPermissoes().clear();
		
		usuario.get().getGrupos().addAll(listaDeGrupoDTO);
		
		return usuario;
	}

}
