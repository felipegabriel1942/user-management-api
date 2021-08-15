package com.felipegabriel.usermanagementapi.api.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

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

	@Override
	public Optional<User> getById(Integer id) {
		return userRepository.findById(id);	
	}

	@Override
	public User update(User user) {
		if (user == null || user.getId() == null) {
			throw new IllegalArgumentException("User id can't be null.");
		}
		
		user.setUpdatedDate(LocalDateTime.now());
		
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		if (user == null || user.getId() == null) {
			throw new IllegalArgumentException("User id can't be null.");
		}
		
		userRepository.delete(user);
	}
}
