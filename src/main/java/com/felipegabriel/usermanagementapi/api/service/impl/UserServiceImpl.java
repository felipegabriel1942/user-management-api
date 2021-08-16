package com.felipegabriel.usermanagementapi.api.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.felipegabriel.usermanagementapi.api.exception.BusinessException;
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
		if (emailAlreadyRegistered(user.getEmail())) {
			throw new BusinessException("There is already a registered user with this e-mail.");
		}
		
		if (loginAlreadyRegistered(user.getLogin())) {
			throw new BusinessException("There is already a registered user with this login.");
		}
			
		return userRepository.save(user);
	}
	
	private boolean emailAlreadyRegistered(String email) {
		return userRepository.findByEmail(email).isPresent();
	}
	
	private boolean loginAlreadyRegistered(String login) {
		return userRepository.findByLogin(login).isPresent();
	}

	@Override
	public Optional<User> getById(Integer id) {
		return userRepository.findById(id);	
	}

	@Override
	public User update(User user) {
		if (userIsInvalid(user)) {
			throw new IllegalArgumentException("User id can't be null.");
		}
		
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		if (userIsInvalid(user)) {
			throw new IllegalArgumentException("User id can't be null.");
		}
		
		userRepository.delete(user);
	}
	
	private boolean userIsInvalid(User user) {
		return user == null || user.getId() == null;
	}

	@Override
	public Page<User> getUsers(Pageable pageRequest) {
		return userRepository.findAll(pageRequest);
	}
}
