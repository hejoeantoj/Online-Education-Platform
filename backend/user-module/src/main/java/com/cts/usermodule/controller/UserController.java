package com.cts.usermodule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.usermodule.dto.LoginDTO;
import com.cts.usermodule.dto.RegistrationDTO;
import com.cts.usermodule.dto.RegistrationResponseDTO;
import com.cts.usermodule.dto.Response;
import com.cts.usermodule.dto.UpdationDTO;
import com.cts.usermodule.exception.EmailAlreadyExistsException;
import com.cts.usermodule.exception.UserNotFoundException;
import com.cts.usermodule.model.User;
import com.cts.usermodule.service.UserService;

import jakarta.validation.Valid;


@RestController
@Validated
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public List<User> getAllUser() {
		return userService.getAllUser();
	}
	
		

     /**
     * Registers a new user.
     * 
     * @param user Registration details of the user.
     * @return ResponseEntity containing the registration response.
     * 
     */


	@PostMapping("/register")
	public ResponseEntity<Response<RegistrationResponseDTO>> registrUser(@Valid @RequestBody RegistrationDTO user) throws EmailAlreadyExistsException {
		Response<RegistrationResponseDTO> response = new Response<>();
		RegistrationResponseDTO userDetails = userService.registerUser(user);
		response.setData(userDetails);
		response.setStatus(HttpStatus.OK);
		response.setSuccess(true);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
     * Updates user details.
     * 
     * @param user Updated details of the user.
     * @return ResponseEntity containing the update response.
     * .
     */


	@PutMapping("/update")
	public ResponseEntity<Response<String>> updateUserDetails(@Valid @RequestBody UpdationDTO user) throws UserNotFoundException {
		Response<String> response = new Response<>();
		String userDetails = userService.updateUserDetails(user);
		response.setData(userDetails);
		response.setStatus(HttpStatus.OK);
		response.setSuccess(true);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
 

     /**
     * Login in a user.
     * @param user Login details of the user.
     * @return ResponseEntity containing the login response.
     * 
     */


	@PostMapping("/login")
	public ResponseEntity<Response<Map<String, String>>> loginUser(@Valid @RequestBody LoginDTO user) throws Exception {
	    Response<Map<String, String>> response = new Response<>();
	    Map<String, String> loginData = userService.loginUser(user);
	    response.setSuccess(true);
	    response.setStatus(HttpStatus.OK);
	    response.setData(loginData);
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
		

     /**
     * Fetches usernames by user IDs.
     * @param userId List of user IDs.
     * @return ResponseEntity containing the usernames.
     */


	@PostMapping("/fetchUsernamesByIds")
    public ResponseEntity<?> fetchUsernamesByIds(@RequestBody List<String> userId) {
        Map<String, String> usernames = new HashMap<>();
        List<User> users = userService.getUsersByIds(userId);

        if (users != null && !users.isEmpty()) {
            
        	usernames=users.stream()
                           .collect(Collectors.toMap(User::getUserId, User::getUserName));
           
        	return ResponseEntity.ok(Map.of("success", true, "message", "Usernames retrieved successfully", "data", usernames));
        } else {
            return ResponseEntity.ok(Map.of("success", true, "message", "No users found for the given IDs", "data", usernames));
        }
    }
	
	
     /**
     * Retrieves user details by user ID.
     * 
     * @param userId ID of the user.
     * @return ResponseEntity containing the user details.
     * 
     */


	@GetMapping("/userDetails")
    public ResponseEntity<Response<User>> getUserDetails(@RequestParam String userId) throws UserNotFoundException {
		Response<User> response = new Response<>();
        User user = userService.getuserDetails(userId);
        response.setSuccess(true);
        response.setStatus(HttpStatus.OK);
        response.setData(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	
	/*
	 * 
	 * Integration with other micro services.
	 * 
	 * 
	 * 
	 */


	@GetMapping("/checkInstructor")
	public boolean checkInstructor(@RequestParam String userId) {
		
		return userService.checkInstructor(userId);
	}

	@GetMapping("/checkStudent")
	public boolean checkStudent(@RequestParam String userId) {

		return userService.checkStudent(userId);
	}

}