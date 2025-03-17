package com.cts.reportsmodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.User;



public interface UserDAO extends JpaRepository<User,String> {

}
