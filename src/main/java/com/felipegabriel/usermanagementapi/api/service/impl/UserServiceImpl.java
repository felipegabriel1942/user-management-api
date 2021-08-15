package com.felipegabriel.usermanagementapi.api.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.model.repository.UserRepository;
import com.felipegabriel.usermanagementapi.api.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;

	@Override
	public User save(User user) {
		user.setCreatedDate(LocalDateTime.now());
		return userRepository.save(user);
	}
}
