package com.cts.usermodule.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cts.usermodule.dto.RegistrationDTO;
import com.cts.usermodule.dto.Response;
import com.cts.usermodule.dto.UpdationDTO;
import com.cts.usermodule.model.User;
import com.cts.usermodule.service.UserService;

import jakarta.servlet.Registration;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public List<User> getAllUser() {
		return userService.getAllUser();
	}

	@PostMapping("/register")
	public ResponseEntity<Response<User>> registrUser( @RequestBody RegistrationDTO user) {
		Response<User> response = new Response<>();
		try {
			User userDetails = userService.registerUser(user);
			response.setData(userDetails);
			response.setStatus(HttpStatus.OK);
			response.setSuccess(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setErrorMessage("CANNOT REGISTER USER");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Response<User>> updateUserDetails(@Valid @RequestBody UpdationDTO user) {
		Response<User> response = new Response<>();
		try {
			User userDetails = userService.updateUserDetails(user);
			response.setData(userDetails);
			response.setStatus(HttpStatus.OK);
			response.setSuccess(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setErrorMessage("CANNOT UPDATE USER");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Response<String>> loginUser( @RequestBody User user) {
		Response<String> response = new Response<>();
		try {
			String token = userService.loginUser(user);
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
			response.setData(token);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setStatus(HttpStatus.UNAUTHORIZED);
			response.setErrorMessage("UNAUTHORIZED	");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
	}
}