package com.felipegabriel.usermanagementapi.api.resource;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.felipegabriel.usermanagementapi.api.dto.EmailDTO;
import com.felipegabriel.usermanagementapi.api.service.EmailService;
import com.felipegabriel.usermanagementapi.api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
@Api("Email API")
public class EmailController {
	
	private final EmailService emailService;
	private final UserService userService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority(\"admin\")")
	@ApiOperation("Send email to user")
	public void sendEmail(@RequestBody @Valid EmailDTO emailDTO) {
		emailService.sendEmail(emailDTO);
	}
	
	@PostMapping("admins")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority(\"admin\")")
	@ApiOperation("Send email to all registered admins")
	public void sendEmailToAllAdmins(@RequestBody @Valid EmailDTO emailDTO) {
		userService.getAllAdmins().forEach(u -> {
			emailDTO.setDestination(u.getEmail());
			emailService.sendEmail(emailDTO);
		});
	}
}
