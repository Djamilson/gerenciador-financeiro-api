package com.example.financeiro.api.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.model.Usuario;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine thymeleaf;

//	@Autowired
//	private LancamentoRepository repo;
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		String template = "mail/aviso-lancamentos-vencidos";
//		
//		List<Lancamento> lista = repo.findAll();
//		
//		Map<String, Object> variaveis = new HashMap<>();
//		variaveis.put("lancamentos", lista);
//		
//		this.enviarEmail("testes.algaworks@gmail.com", 
//				Arrays.asList("alexandre.algaworks@gmail.com"), 
//				"Testando", template, variaveis);
//		System.out.println("Terminado o envio de e-mail...");
//	}

	public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", vencidos);

		List<String> emails = destinatarios.stream().map(u -> u.getEmail()).collect(Collectors.toList());

		this.enviarEmail("testes.algaworks@gmail.com", emails, "Lançamentos vencidos",
				"mail/aviso-lancamentos-vencidos", variaveis);
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template,
			Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));

		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

		String mensagem = thymeleaf.process(template, context);

		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
	}
	
	
	
	/* ============================================================*/
	
	public void recuperarSenha(Usuario usuario, String token) {
		System.out.println("Nome: "+usuario.getNome());
		System.out.println("Token: "+token);

		String email = "http://localhost:4200/usuarios/recuperar-senhas/".concat(token);

		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("nome", usuario.getNome());
		variaveis.put("token", token);
		variaveis.put("email", email);

		this.enviarEmailRecuperaSenha("djamilsonapp@gmail.com", usuario, "Recuperação de senha","mail/recuperar-senha", variaveis);
	}
	
	public void enviarEmailRecuperaSenha(String remetente, Usuario usuario, String assunto, String template, Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));


		
		/*
		variaveis.entrySet().forEach(e -> {
			System.out.println("===>>> Key: "+e.getKey()+ ">>+ Value: "+ e.getValue());
			context.setVariable(e.getKey(), e.getValue());
			}); */
		
		 // Set additional variables
        variaveis.forEach((key, value) -> context.setVariable(key, value));
        
		
		System.out.println("Nome: "+usuario.getNome());
		System.out.println("context 1: "+context.toString());
		System.out.println("context 2: "+context.getVariableNames());

		System.out.println("context 2: "+context.getVariableNames());
		
		System.out.println("context 3: "+context.getClass().getName());
		System.out.println("context 4: "+context.getClass().getTypeName());
		System.out.println("context 5: "+context.getClass().getSimpleName());

		System.out.println("context 6: "+context.getClass().getCanonicalName());

		System.out.println("context 7: "+context.getClass().getModifiers());

	
		String mensagem = thymeleaf.process(template, context);

		
		this.enviarEmailRecuperarSenha(remetente, usuario.getEmail(), assunto, mensagem);
	}

	public void enviarEmailRecuperarSenha(String remetente, String destinatario, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatario);
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail de recuperção de senha!", e);
		}
	}

	
}
