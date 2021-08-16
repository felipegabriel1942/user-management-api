package com.felipegabriel.usermanagementapi.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	private Integer id;
	
	@NotBlank(message = "Name is obrigatory")
	private String name;
	
	@NotBlank(message = "Password is obrigatory")
	private String password;
	
	@NotBlank(message = "E-mail is obrigatory")
	@Email(message = "E-mail is not valid")
	private String email;
	
	@NotBlank(message = "Login is obrigatory")
	private String login;
	
	private boolean admin;
	
}
