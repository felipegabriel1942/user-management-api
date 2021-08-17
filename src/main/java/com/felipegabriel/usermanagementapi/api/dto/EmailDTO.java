package com.felipegabriel.usermanagementapi.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmailDTO {
	
	@Email(message = "E-mail is not valid.")
	private String destination;
	
	@NotBlank(message = "Subject is obrigatory.")
	private String subject;
	
	@NotBlank(message = "Text is obrigatory.")
	private String text;
}
