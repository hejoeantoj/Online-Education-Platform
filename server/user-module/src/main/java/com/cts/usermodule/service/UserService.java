package com.cts.usermodule.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.usermodule.dao.UserDAO;
import com.cts.usermodule.dto.LoginDTO;
import com.cts.usermodule.dto.RegistrationDTO;
import com.cts.usermodule.dto.RegistrationResponseDTO;
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
	

	public RegistrationResponseDTO registerUser(RegistrationDTO user) {
		
		User newUser = new User();
		newUser.setUserName(user.getUserName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(encoder.encode(user.getPassword()));
		newUser.setRole(user.getRole());		
		userDAO.save(newUser);
		
		RegistrationResponseDTO response=new RegistrationResponseDTO();
		response.setUserName(user.getUserName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setCreatedAt(LocalDateTime.now());
		return response;
	}
	
	
	public String loginUser(LoginDTO user) {
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
	    User authUser=userDAO.findByEmail(user.getEmail());
	    
	    if(authUser.getRole()!=user.getRole()) {
	    	throw new UserNotFoundException("Forbidden");
	    }
		if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail(), user.getRole());
        } else {
            throw new UserNotFoundException("Invalid credentials");
        }
    
	}
	
	
	public String updateUserDetails(UpdationDTO user) {

		User oldUser= userDAO.findByEmail(user.getEmail());
		
		if(oldUser == null) {
			throw new UserNotFoundException("No User Found With Email: " + user.getEmail());
		}
		
		oldUser.setUserName(user.getUserName());
		oldUser.setPassword(encoder.encode(user.getPassword()));
		oldUser.setEmail(user.getEmail());
		userDAO.save(oldUser);
		
		return "Updated Successfully";
	}


	public List<User> getAllUser() {
		return userDAO.findAll();
	}

}
