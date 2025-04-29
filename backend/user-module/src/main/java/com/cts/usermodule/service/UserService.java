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
import com.cts.usermodule.exception.EmailAlreadyExistsException;
import com.cts.usermodule.exception.UserNotFoundException;
import com.cts.usermodule.model.User;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private AuthenticationManager authManager;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
		

     /**
     * Registers a new user.
     * 
     * @param user Registration details of the user.
     * @return RegistrationResponseDTO containing the registered user's details.
     * 
     */


	public RegistrationResponseDTO registerUser(RegistrationDTO user) {

		if (userDAO.existsByEmail(user.getEmail())) {
			throw new EmailAlreadyExistsException("Email Already Exists: " + user.getEmail());
		}

		User newUser = new User();
		newUser.setUserName(user.getUserName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(encoder.encode(user.getPassword()));
		newUser.setRole(user.getRole());
		userDAO.save(newUser);

		RegistrationResponseDTO response = new RegistrationResponseDTO();
		response.setUserName(user.getUserName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setCreatedAt(LocalDateTime.now());
		return response;
	}
	

     /**
     * Logs in a user.
     * 
     * @param user Login details of the user.
     * @return Map containing the JWT token and user UUID.
     * 
     */


	public Map<String, String> loginUser(LoginDTO user) {
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		User authUser = userDAO.findByEmail(user.getEmail());

		if (!authUser.getRole().equals(user.getRole())) {
			throw new UserNotFoundException("Forbidden");
		}

		if (authentication.isAuthenticated()) {
			String token = jwtService.generateToken(user.getEmail(), user.getRole());
			String uuid = authUser.getUserId();

			Map<String, String> loginData = new HashMap<>();
			loginData.put("token", token);
			loginData.put("uuid", uuid);
			return loginData;
		} else {
			throw new UserNotFoundException("Invalid credentials");
		}
	}

	

     /**
     * Updates user details.
     * @param user Updated details of the user.
     * @return String message indicating the update status.
     */

	public String updateUserDetails(UpdationDTO user) {

		User oldUser = userDAO.findByEmail(user.getEmail());

		if (oldUser == null) {
			throw new UserNotFoundException("No User Found With Email: " + user.getEmail());
		}

		oldUser.setUserName(user.getUserName());
		oldUser.setPassword(encoder.encode(user.getPassword()));
		oldUser.setEmail(user.getEmail());
		userDAO.save(oldUser);

		return "Updated Successfully";
	}

	

     /**
     * Retrieves users by their IDs.
     * 
     * @param userId List of user IDs.
     * @return List of users.
     */

	public List<User> getUsersByIds(List<String> userId) {
		List<User> listObject = userDAO.findAllById(userId);
		return listObject;
	}
	

     /**
     * Retrieves user details by user ID.
     * 
     * @param userId ID of the user.
     * @return User details.
     * 
     */

	
	public User getuserDetails(String userId) {
		User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not found"));

		return user;
	}
	
	
	
	public List<User> getAllUser() {
		return userDAO.findAll();
	}

	
	
	/*
	 * 
	 * 
	 * Integration purposes.
	 * 
	 * 
	 * 
	 */
	
	public boolean checkInstructor(String userId) {

		User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not found"));

		if ("INSTRUCTOR".equals(user.getRole().toString())) {
			return true;
		} else {

			return false;
		}

	}

	public boolean checkStudent(String userId) {
		User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not found"));

		if ("STUDENT".equals(user.getRole().toString())) {
			return true;
		} else {

			return false;
		}

	}





}
