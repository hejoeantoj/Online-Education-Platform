package com.cts.usermodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.usermodule.model.User;

public interface UserDAO extends JpaRepository <User,String> {
	


	User findByUserName(String username);

}
