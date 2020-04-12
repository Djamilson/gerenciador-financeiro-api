package com.example.financeiro.api.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Imagem.class)
public abstract class Imagem_ {

	public static volatile SingularAttribute<Imagem, String> caminho_s3;
	public static volatile SingularAttribute<Imagem, Long> codigo;
	public static volatile SingularAttribute<Imagem, Lancamento> lancamento;
	public static volatile SingularAttribute<Imagem, LocalDateTime> dataCad;
	public static volatile SingularAttribute<Imagem, String> nome;

}

