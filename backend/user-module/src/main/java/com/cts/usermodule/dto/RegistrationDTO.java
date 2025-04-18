package com.cts.usermodule.dto;

import com.cts.usermodule.enums.Roles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegistrationDTO {
	
	
	@NotEmpty(message="Give UserName")
	private String userName;
	

	
	@NotEmpty(message="Give Password")
	private String password;
	
	@Email(message="Give Email")
	@NotEmpty
	private String email;
	

	@NotNull(message = "Give proper role for Role......")
	private Roles role;


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Roles getRole() {
		return role;
	}


	public void setRole(Roles role) {
		this.role = role;
	}
	
	

}
