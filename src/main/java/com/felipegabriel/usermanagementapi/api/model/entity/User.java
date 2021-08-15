package com.felipegabriel.usermanagementapi.api.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "Name is obrigatory")
	private String name;
	
	@NotBlank(message = "Password is obrigatory")
	private String password;
	
	@NotBlank(message = "Creation Date is obrigatory")
	private LocalDateTime createdDate;
	
	private LocalDateTime updatedDate;

	@NotBlank(message = "E-mail is obrigatory")
	private String email;

	private boolean admin;
}
