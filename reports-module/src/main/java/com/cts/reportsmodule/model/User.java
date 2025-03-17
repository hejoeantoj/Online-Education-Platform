package com.cts.reportsmodule.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.cts.reportsmodule.enums.Roles;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    private String userId;

    @Column(name="userName" ,nullable = false, unique = true)
    private String userName;

    @Column(name="email",nullable = false, unique = true)
    private String email;

    @Column(name="password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "createdAt",nullable=false)
    private LocalDateTime createdAt;

    @PrePersist
    public void generateUserIdandDate() {
        this.userId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
