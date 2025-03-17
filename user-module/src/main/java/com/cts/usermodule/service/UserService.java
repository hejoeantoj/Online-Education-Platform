package com.cts.usermodule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.usermodule.dao.UserDAO;
import com.cts.usermodule.dto.RegistrationDTO;
import com.cts.usermodule.dto.UpdationDTO;
import com.cts.usermodule.model.User;
import com.cts.usermodule.enums.Roles;
import com.cts.usermodule.exception.UserNotFoundException;


@Service
public class UserService {
	
	
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
	

	public User registerUser(RegistrationDTO user) {
		
		User newUser = new User();
		newUser.setUserName(user.getUserName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(encoder.encode(user.getPassword()));
		newUser.setRole(user.getRole());
		
		return userDAO.save(newUser);
	}
	
	
	public String loginUser(User user) {
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
		if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUserName());
        } else {
            throw new UserNotFoundException("Invalid credentials");
        }
    
	}
	
	
	public User updateUserDetails(UpdationDTO user) {

		User oldUser= userDAO.findByUserName(user.getEmail());
		
		if(oldUser == null) {
			throw new UserNotFoundException("No User Found With Email: " + user.getEmail());
		}
		
		oldUser.setUserName(user.getUserName());
		oldUser.setPassword(encoder.encode(user.getPassword()));
		oldUser.setEmail(user.getEmail());
		
		return userDAO.save(oldUser);
	}


	public List<User> getAllUser() {
		return userDAO.findAll();
	}

}
