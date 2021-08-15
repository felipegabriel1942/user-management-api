package com.felipegabriel.usermanagementapi.api.service;

import java.util.Optional;

import com.felipegabriel.usermanagementapi.api.model.entity.User;


public interface UserService {
	
	User save(User user);
	
	Optional<User> getById(Integer id);
	
	User update(User user);
	
	void delete(User user);
	
	
}
