package com.felipegabriel.usermanagementapi.api.config;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;

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
	public void populateInMemoryDatabase() throws NoSuchAlgorithmException {
		User user = User.builder()
				.admin(true)
				.name("admin")
				.login("admin")
				.email("admin@admin.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();

		User user2 = User.builder()
				.admin(false)
				.name("user")
				.login("user")
				.email("user@user.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user3 = User.builder()
				.admin(false)
				.name("Noah Rogers")
				.login("Noah Rogers")
				.email("noah_rogers@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user4 = User.builder()
				.admin(false)
				.name("Darius Goulding")
				.login("Darius Goulding")
				.email("darius_goulding@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user5 = User.builder()
				.admin(false)
				.name("Donna Thorpe")
				.login("Donna Thorpe")
				.email("donna_thorpeg@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user6 = User.builder()
				.admin(false)
				.name("Iosif Blevins")
				.login("Iosif Blevins")
				.email("iosif_blevins@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user7 = User.builder()
				.admin(false)
				.name("Yolanda Cartwright")
				.login("Yolanda Cartwright")
				.email("yolanda_cartwright@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user8 = User.builder()
				.admin(false)
				.name("Effie Young")
				.login("Effie Young")
				.email("effie_young@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user9 = User.builder()
				.admin(false)
				.name("Carley Briggs")
				.login("Carley Briggs")
				.email("carley_briggs@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user10 = User.builder()
				.admin(false)
				.name("Taylah Santos")
				.login("Taylah Santos")
				.email("taylah_santos@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		User user11 = User.builder()
				.admin(false)
				.name("Bethany Iles")
				.login("Bethany Iles")
				.email("bethany_iles@gmail.com")
				.password(Md5.md5Encripter("123"))
				.createdDate(LocalDateTime.now()).build();
		
		Arrays.asList(user, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11).forEach(userService::save);
	}
	
}
