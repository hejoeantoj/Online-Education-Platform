package com.cts.usermodule.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cts.usermodule.exception.UserAlreadyExistsException;
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
		
		if(userDAO.existsByEmail(user.getEmail())){
			throw new UserAlreadyExistsException("Email Already Exists: " + user.getEmail());
		}
		
		if(userDAO.existsByUserName(user.getUserName())){
			throw new UserAlreadyExistsException("UserName Already Exists: " + user.getUserName());
		}
		
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
	
	
	public Map<String, String> loginUser(LoginDTO user) {
	    Authentication authentication = authManager.authenticate(
	        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
	    );
	    User authUser = userDAO.findByEmail(user.getEmail());

	    if (!authUser.getRole().equals(user.getRole())) {
	        throw new UserNotFoundException("Forbidden");
	    }

	    if (authentication.isAuthenticated()) {
	        String token = jwtService.generateToken(user.getEmail(), user.getRole());
	        String uuid = authUser.getUserId(); // Assuming you have a method to get the user's UUID

	        Map<String, String> loginData = new HashMap<>();
	        loginData.put("token", token);
	        loginData.put("uuid", uuid);

	        return loginData;
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


	
	
	
	
	
	
	public boolean checkInstructor(String userId) {
	
		User user=userDAO.findById(userId)
				.orElseThrow(()-> new UserNotFoundException("User Not found"));
		
		
		if ("INSTRUCTOR".equals(user.getRole().toString())) {
			return true;
		}else {
			
			return false;
		}
			
	}


	public boolean checkStudent(String userId) {
		User user=userDAO.findById(userId)
				.orElseThrow(()-> new UserNotFoundException("User Not found"));
		
		
		if ("STUDENT".equals(user.getRole().toString())) {
			return true;
		}else {
			
			return false;
		}
			
	}


	public List<User> getUsersByIds(List<String> userId) {
		List<User> listObject=userDAO.findAllById(userId);
		return listObject;
	}


	public User getuserDetails(String userId) {
		User user=userDAO.findById(userId)
				.orElseThrow(()-> new UserNotFoundException("User Not found"));
		
		
		return user;
	}

}
