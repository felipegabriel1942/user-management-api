package com.felipegabriel.usermanagementapi.api.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.felipegabriel.usermanagementapi.api.model.entity.User;

import lombok.Getter;
import lombok.Setter;

public class UserDetail extends User implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Integer id;
	
	@Getter
	@Setter
	private String login;
	
	@Getter
	@Setter
	private String userPassword;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.getLogin();
	}
	
	@Override
	public String getPassword() {
		return this.getUserPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
