package com.cts.usermodule.dto;

import com.cts.usermodule.enums.Roles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LoginDTO {
	
	
	@Email(message="Give Email")
	private String email;
	
	@NotEmpty(message="Give Password ")
	private String password;
	
	@NotNull(message="Give Role")
	private Roles role;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	
	
	
	

}
