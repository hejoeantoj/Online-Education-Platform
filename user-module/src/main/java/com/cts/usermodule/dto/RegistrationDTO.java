package com.cts.usermodule.dto;

import com.cts.usermodule.enums.Roles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegistrationDTO {
	
	
	@NotBlank(message="please provide userName")
	@NotNull(message="UserName should not be null")
	private String userName;
	

	@NotBlank(message="please provide password")
	@NotNull(message="password should not be null")
	private String password;
	

	@NotBlank(message="please provide email")
	@NotNull(message="Email should not be null")
	private String email;
	

	@NotNull(message = "Role cannot be null")
    @NotBlank(message = "Role cannot be blank")
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
