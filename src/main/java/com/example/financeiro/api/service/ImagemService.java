package com.example.financeiro.api.service;

import java.awt.image.ImagingOpException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.financeiro.api.dto.ImagemDTO;
import com.example.financeiro.api.model.Imagem;
import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.repository.ImagemRepository;
import com.example.financeiro.api.storage.S3;


@Service
public class ImagemService {

	@Autowired
	private ImagemRepository imagemRepository;

	@Autowired
	private S3 s3;
			
	public Imagem salvar(Imagem imagem) {		
		return imagemRepository.save(imagem);
	}
	
	public void anexarOsDados(MultipartFile[] anexo, Lancamento lancamento)  {
		
		LocalDateTime dataHoraAgora = LocalDateTime.now();
		//  List<Imagem> listadeImagens = new ArrayList<>();
		  String nome = "";		  
		
		try {
	        for (MultipartFile file : anexo) {
	        	
	        	System.out.println("File: "+file.getName());		      	
	        	System.out.println("File: "+file.getOriginalFilename());
	        	// System.out.println("lastModifiedDate: "+ file.lastModifiedDate);
	          		        	
	        	Imagem imagem = new Imagem();
	        	nome = s3.salvarTemporariamente(file); // deve fica aqui, pois só vai criar o caminho se for salvo no bancket
	        		        	
	        	imagem.setCaminho_s3(s3.configurarUrl(nome));
	        	imagem.setNome(file.getOriginalFilename());
	        	imagem.setDataCad(dataHoraAgora);
	        	imagem.setLancamento(lancamento);	        	
	 
	        	salvar(imagem);//salva a imagem no banco
	        		        	    			        		 
	        }
	        
	        //lancamento.setLista(listadeImagens);

	    } catch (ImagingOpException e) {
	        e.printStackTrace();
	    }
						
	}
	
	public List<ImagemDTO> listaImagemDTO(Long codigo){
		
		List<ImagemDTO> listaImagemDTO = new ArrayList<>();

		imagemRepository.findByLancamentoCodigo(codigo).forEach(imagem -> {
			ImagemDTO i = new ImagemDTO();
			i.setCodigo(imagem.getCodigo());
			i.setNome(imagem.getNome());
			i.setCaminho_s3(imagem.getCaminho_s3());
			i.setDataCad(imagem.getDataCad());
			i.setCodigo_lancamento(imagem.getLancamento().getCodigo());
			listaImagemDTO.add(i);
		});

		return listaImagemDTO;
	}

	
}
