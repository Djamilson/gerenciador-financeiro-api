package com.example.financeiro.api.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VerificationToken.class)
public abstract class VerificationToken_ {

	public static volatile SingularAttribute<VerificationToken, LocalDateTime> expiryDate;
	public static volatile SingularAttribute<VerificationToken, Long> codigo;
	public static volatile SingularAttribute<VerificationToken, Usuario> user;
	public static volatile SingularAttribute<VerificationToken, String> token;

}

