package com.felipegabriel.usermanagementapi.api.config;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.security.Md5;
import com.felipegabriel.usermanagementapi.api.service.UserService;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class DevConfig {
	
	private final UserService userService;
	
	@Bean
	public void createUser() throws NoSuchAlgorithmException {
		User user = User.builder()
				.admin(true)
				.name("admin")
				.login("admin")
				.email("admin@admin.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		userService.save(user);
	}
	
}
