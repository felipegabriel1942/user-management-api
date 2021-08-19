package com.felipegabriel.usermanagementapi.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.mail.javamail.JavaMailSender;

import com.felipegabriel.usermanagementapi.api.service.impl.EmailServiceImpl;

public class EmailServiceTest {
	
	private EmailService emailService;
	
	private JavaMailSender javaMailSender;
	
	@BeforeEach
	public void setUp() {
		this.emailService = new EmailServiceImpl(javaMailSender);
	}
}
