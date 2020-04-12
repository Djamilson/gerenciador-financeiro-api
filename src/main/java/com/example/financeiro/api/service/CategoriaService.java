package com.example.financeiro.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.financeiro.api.model.Categoria;
import com.example.financeiro.api.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria atualizar(Long codigo, Categoria categoria) {

		/*
		 * Optional<Categoria> categoriaExistente =
		 * categoriaRepository.findByNomeContaining(categoria.getNome());
		 * 
		 * if (categoriaExistente.get() != null &&
		 * !categoriaExistente.get().getNome().equals(categoria.getNome())) { return
		 * ResponseEntity.status("405").body(categoriaSalva); }
		 */
		Categoria categoriaSalva = buscarCategoriaPeloCodigo(codigo);

		BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");
		return categoriaRepository.save(categoriaSalva);
	}

	public Categoria buscarCategoriaPeloCodigo(Long codigo) {
		Optional<Categoria> categoriaSalva = categoriaRepository.findById(codigo);
		if (!categoriaSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return categoriaSalva.get();
	}

	public Categoria buscarCategoriaPeloNome(String nome) {
		Optional<Categoria> categoriaSalva = categoriaRepository.findByNomeContaining(nome);
		if (!categoriaSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return categoriaSalva.get();
	}
}
