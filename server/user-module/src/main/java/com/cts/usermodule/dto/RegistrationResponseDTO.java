package com.cts.usermodule.dto;

import java.time.LocalDateTime;

import com.cts.usermodule.enums.Roles;

public class RegistrationResponseDTO {
	
	
    private String userName;
	
    
	private String email;
	
	
	private LocalDateTime createdAt;
	
	
	private Roles role;


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


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public Roles getRole() {
		return role;
	}


	public void setRole(Roles role) {
		this.role = role;
	}
	
	
	
	

}
