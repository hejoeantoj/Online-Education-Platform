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
import com.cts.usermodule.exception.UserAlreadyExistsException;
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
		catch (UserAlreadyExistsException e) {
			response.setSuccess(false);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setErrorMessage("User/Email Already Exists");
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
	public ResponseEntity<Response<Map<String, String>>> loginUser(@Valid @RequestBody LoginDTO user) {
	    Response<Map<String, String>> response = new Response<>();
	    try {
	        Map<String, String> loginData = userService.loginUser(user);
	        response.setSuccess(true);
	        response.setStatus(HttpStatus.OK);
	        response.setData(loginData);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception e) {
	        response.setSuccess(false);
	        response.setStatus(HttpStatus.UNAUTHORIZED);
	        response.setErrorMessage("Bad Credentials");
	        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	    }
	}
	
	
	@PostMapping("/fetchUsernamesByIds")
    public ResponseEntity<?> fetchUsernamesByIds(@RequestBody List<String> userId) {
        Map<String, String> usernames = new HashMap<>();
        List<User> users = userService.getUsersByIds(userId);

        if (users != null && !users.isEmpty()) {
            usernames.putAll(users.stream()
                    .collect(Collectors.toMap(User::getUserId, User::getUserName)));
            return ResponseEntity.ok(Map.of("success", true, "message", "Usernames retrieved successfully", "data", usernames));
        } else {
            return ResponseEntity.ok(Map.of("success", true, "message", "No users found for the given IDs", "data", usernames));
        }
    }
	
	
	@GetMapping("/userDetails")
    public ResponseEntity<?> getUserDetails(@RequestParam String userId) {
		Response<User> response = new Response<>();
		try {
        User user = userService.getuserDetails(userId);
        response.setSuccess(true);
        response.setStatus(HttpStatus.OK);
        response.setData(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
		}
        catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setErrorMessage("");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
       
    }

	
	
	
	
	
	
	@GetMapping("/checkInstructor")
	public boolean checkInstructor(@RequestParam String userId) {
		System.out.print("HIIII from user");
		return userService.checkInstructor(userId);
	}
	
	@GetMapping("/checkStudent")
	public boolean checkStudent(@RequestParam String userId) {
		System.out.print("HIIII from user");
		return userService.checkStudent(userId);
	}
	
}