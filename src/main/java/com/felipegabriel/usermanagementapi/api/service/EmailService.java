package com.felipegabriel.usermanagementapi.api.service;

import com.felipegabriel.usermanagementapi.api.dto.EmailDTO;

public interface EmailService {
	
	void sendEmail(EmailDTO emailDTO);
}
