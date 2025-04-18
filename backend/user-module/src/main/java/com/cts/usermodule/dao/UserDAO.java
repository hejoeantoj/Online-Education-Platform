package com.cts.usermodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.usermodule.model.User;

public interface UserDAO extends JpaRepository <User,String> {
	


	User findByEmail(String username);

	boolean existsByEmail(String email);

	boolean existsByUserName(String userName);

}
