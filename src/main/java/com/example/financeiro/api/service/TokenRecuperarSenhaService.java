package com.example.financeiro.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.financeiro.api.model.Usuario;
import com.example.financeiro.api.model.VerificationToken;
import com.example.financeiro.api.repository.UsuarioRepository;
import com.example.financeiro.api.repository.VerificationTokenRepository;

@Service
public class TokenRecuperarSenhaService {

	@Autowired
	private VerificationTokenRepository tokenRepository;

	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	/*
	 * public VerificationToken getVerificationToken(final String VerificationToken)
	 * {
	 * 
	 * return tokenRepository.findByToken(VerificationToken); }
	 * 
	 * public void deleteUser(final Usuario user) { final VerificationToken
	 * verificationToken = tokenRepository.findByUser(user);
	 * 
	 * if (verificationToken != null) { tokenRepository.delete(verificationToken); }
	 * 
	 * usuarioRepository.delete(user); }
	 */

	private void testaeDeletaverificationToken(final Usuario user) {
		VerificationToken verificationToken = tokenRepository.findByUser(user);
		// se tiver token remover
		if (verificationToken != null) {
			tokenRepository.delete(verificationToken);
		}
	}

	public void createVerificationTokenForUser(final Usuario user, final String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		
		testaeDeletaverificationToken(user);
		
		tokenRepository.save(myToken);

	}

	/*
	 * public VerificationToken generateNewVerificationToken(final String
	 * existingVerificationToken) { VerificationToken vToken =
	 * tokenRepository.findByToken(existingVerificationToken);
	 * vToken.updateToken(UUID.randomUUID().toString()); vToken =
	 * tokenRepository.save(vToken); return vToken; }
	 * 
	 */
	public String validateVerificationToken(VerificationToken verificationToken) {
		LocalDateTime DataHoraAgora = LocalDateTime.now();

		String strLocalDateTime3 = verificationToken.getExpiryDate()
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		String strLocalDateTime2 = DataHoraAgora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		// String localDateTime6 = LocalDateTime.parse(DataHoraAgora,
		// DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		System.out.println("Data expira? " + verificationToken.getExpiryDate());
		System.out.println("DataHoraAgora? " + strLocalDateTime2);

		if (verificationToken == null) {
			return TOKEN_INVALID;
		}

		// System.out.println("As datas são iguais?
		// "+DataHoraAgora.equals(verificationToken.getExpiryDate()));
		// Comparando se já passou das 11:00
		// System.out.println("Já passou das 11:00?" +
		// DataHoraAgora.isAfter(verificationToken.getExpiryDate()));

		if (DataHoraAgora.isAfter(verificationToken.getExpiryDate())) {
			// tokenRepository.delete(verificationToken);
			System.out.println("Tem que cria novo Token !! " + verificationToken.getExpiryDate());
			//remove o token
			testaeDeletaverificationToken(verificationToken.getUser());
			
			return TOKEN_EXPIRED;
		}

		System.out.println("Token ainda não expirou!!! " + verificationToken.getExpiryDate());

		return TOKEN_VALID;
	}

	/*
	 * private boolean emailExist(final String email) { return
	 * usuarioRepository.findByEmailAndAtivoEquals(email, true) != null; }
	 */

}