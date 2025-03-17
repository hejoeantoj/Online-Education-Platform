package com.cts.usermodule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdationDTO {
	
	@NotBlank(message="please provide userName")
	@NotNull(message="UserName should not be null")
	private String userName;
	
	@NotBlank(message="please provide userName")
	@NotNull(message="UserName should not be null")
	private String email;
	
	@NotBlank(message="please provide userName")
	@NotNull(message="UserName should not be null")
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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
	
	
	
	

}
