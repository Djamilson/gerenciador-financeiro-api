package com.example.financeiro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.model.VerificationToken;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	public Optional<VerificationToken> findByToken(String token);
	
   // VerificationToken findByToken(String token);

    VerificationToken findByUser(Usuario user);
/*
    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now); */
}