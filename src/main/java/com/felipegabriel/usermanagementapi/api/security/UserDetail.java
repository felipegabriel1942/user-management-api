package com.felipegabriel.usermanagementapi.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
	
	@Getter
	@Setter
	private List<String> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<>();
		
		roles.stream().forEach(r -> list.add(new SimpleGrantedAuthority(r)));
		
		return list;
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
