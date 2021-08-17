package com.felipegabriel.usermanagementapi.api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.felipegabriel.usermanagementapi.api.dto.EmailDTO;
import com.felipegabriel.usermanagementapi.api.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	
	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	private EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(EmailDTO emailDTO) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(emailDTO.getDestination());
		message.setSubject(emailDTO.getSubject());
		message.setText(emailDTO.getText());
		
		javaMailSender.send(message);
	}
}
