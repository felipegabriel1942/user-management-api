package com.felipegabriel.usermanagementapi.api.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

public class ApiErrors {
	
	@Getter
	private List<String> errors;
	
	public ApiErrors(BindingResult bindingResult) {
		this.errors = new ArrayList<>();
		bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
	}
	
	public ApiErrors(ResponseStatusException ex) {
		this.errors = Arrays.asList(ex.getReason());
	}
	
}
