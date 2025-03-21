package com.cts.usermodule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.usermodule.dto.LoginDTO;
import com.cts.usermodule.dto.RegistrationDTO;
import com.cts.usermodule.dto.RegistrationResponseDTO;
import com.cts.usermodule.dto.Response;
import com.cts.usermodule.dto.UpdationDTO;
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

	@PostMapping("/register")
	public ResponseEntity<Response<?>> registrUser(@Valid @RequestBody RegistrationDTO user) {
		Response<RegistrationResponseDTO> response = new Response<>();
		try {
			RegistrationResponseDTO userDetails = userService.registerUser(user);
			
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
	public ResponseEntity<Response<String>> updateUserDetails(@Valid @RequestBody UpdationDTO user) {
		Response<String> response = new Response<>();
		try {
			String userDetails = userService.updateUserDetails(user);
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
	public ResponseEntity<Response<String>> loginUser(@Valid @RequestBody LoginDTO user) {
		Response<String> response = new Response<>();
		try {
			String token = userService.loginUser(user);
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
			response.setData(token);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			
		}
	}
}