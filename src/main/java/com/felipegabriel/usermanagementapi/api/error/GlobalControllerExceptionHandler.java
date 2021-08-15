package com.felipegabriel.usermanagementapi.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.felipegabriel.usermanagementapi.api.exceptions.ApiErrors;
import com.felipegabriel.usermanagementapi.api.exceptions.BusinessException;


@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@SuppressWarnings("unchecked")
	@ExceptionHandler(ResponseStatusException.class)
	@ResponseStatus
	public ResponseEntity<?> handleResponseStatusException(ResponseStatusException exception) {
		return new ResponseEntity<>(new ApiErrors(exception), exception.getStatus());
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handlerBusinessException(BusinessException exception) {
		return new ApiErrors(exception);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		return new ApiErrors(bindingResult);
	}
	
}
