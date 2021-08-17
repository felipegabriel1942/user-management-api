package com.felipegabriel.usermanagementapi.api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.felipegabriel.usermanagementapi.api.enums.UserRoles;
import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.model.repository.UserRepository;



@Repository
public class UserDetailImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserDetail userDetail = new UserDetail();

		User user = userRepository.findByLogin(username).orElse(null);

		if (user == null) {
			throw new UsernameNotFoundException("Login or password invalid.");
		}

		userDetail.setId(user.getId());
		userDetail.setLogin(user.getLogin());
		userDetail.setUserPassword(user.getPassword());
		userDetail.setRoles(getUserRoles(user));

		return userDetail;
	}
	
	private List<String> getUserRoles(User user) {
		List<String> authorities = new ArrayList<>();
		
		if (user.isAdmin()) {
			authorities.add(UserRoles.ADMIN.getValue());
		} else {
			authorities.add(UserRoles.USER.getValue());
		}
		
		return authorities;
	}
}
