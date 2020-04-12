package com.example.financeiro.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.financeiro.api.model.Grupo;
import com.example.financeiro.api.repository.GrupoRepository;

@Service
public class GrupoService {

	@Autowired
	private GrupoRepository grupoRepository;

	public Grupo salvar(Grupo grupo) {
		return grupoRepository.save(grupo);
	}

	public Grupo atualizar(Long codigo, Grupo grupo) {
		Grupo grupoSalvo = buscarGrupoPeloCodigo(codigo);

		grupoSalvo.getPermissoes().clear();
		grupoSalvo.getPermissoes().addAll(grupo.getPermissoes());

		BeanUtils.copyProperties(grupo, grupoSalvo, "codigo", "grupos");
		return grupoRepository.save(grupoSalvo);
	}

	public Grupo buscarGrupoPeloCodigo(Long codigo) {
		Optional<Grupo> grupoSalvo = grupoRepository.findById(codigo);
		if (!grupoSalvo.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return grupoSalvo.get();
	}

	

}
