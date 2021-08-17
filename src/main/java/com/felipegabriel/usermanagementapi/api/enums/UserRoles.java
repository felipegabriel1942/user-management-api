package com.felipegabriel.usermanagementapi.api.enums;

public enum UserRoles {
	
	USER("user"),
	ADMIN("admin");
	
	private String value;
	
	UserRoles(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
