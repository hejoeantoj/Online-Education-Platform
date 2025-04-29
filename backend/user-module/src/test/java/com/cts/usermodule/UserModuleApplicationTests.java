package com.cts.usermodule;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cts.usermodule.dao.UserDAO;
import com.cts.usermodule.dto.LoginDTO;
import com.cts.usermodule.dto.RegistrationDTO;
import com.cts.usermodule.dto.RegistrationResponseDTO;
import com.cts.usermodule.dto.UpdationDTO;
import com.cts.usermodule.enums.Roles;
import com.cts.usermodule.exception.EmailAlreadyExistsException;
import com.cts.usermodule.exception.UserNotFoundException;
import com.cts.usermodule.model.User;
import com.cts.usermodule.service.JWTService;
import com.cts.usermodule.service.UserService;

public class UserModuleApplicationTests {

    @Mock
    private UserDAO userDAO;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private User mockUser;
    private RegistrationDTO mockRegistrationDTO;
    private LoginDTO mockLoginDTO;
    private UpdationDTO mockUpdationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setUserId(UUID.randomUUID().toString());
        mockUser.setUserName("Test User");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword(encoder.encode("password"));
        mockUser.setRole(Roles.STUDENT);
        mockUser.setCreatedAt(LocalDateTime.now());

        mockRegistrationDTO = new RegistrationDTO();
        mockRegistrationDTO.setUserName("New User");
        mockRegistrationDTO.setEmail("new@example.com");
        mockRegistrationDTO.setPassword("newpassword");
        mockRegistrationDTO.setRole(Roles.INSTRUCTOR);

        mockLoginDTO = new LoginDTO();
        mockLoginDTO.setEmail("test@example.com");
        mockLoginDTO.setPassword("password");
        mockLoginDTO.setRole(Roles.STUDENT);

        mockUpdationDTO = new UpdationDTO();
        mockUpdationDTO.setUserName("Updated User");
        mockUpdationDTO.setEmail("test@example.com");
        mockUpdationDTO.setPassword("updatedpassword");
    }

    @Test
    void testRegisterUser_Success() {
        when(userDAO.existsByEmail(mockRegistrationDTO.getEmail())).thenReturn(false);
        when(userDAO.save(any(User.class))).thenReturn(mockUser);

        RegistrationResponseDTO response = userService.registerUser(mockRegistrationDTO);

        assertNotNull(response);
        assertEquals(mockRegistrationDTO.getUserName(), response.getUserName());
        assertEquals(mockRegistrationDTO.getEmail(), response.getEmail());
        assertEquals(mockRegistrationDTO.getRole(), response.getRole());
        assertNotNull(response.getCreatedAt());
        verify(userDAO, times(1)).existsByEmail(mockRegistrationDTO.getEmail());
        verify(userDAO, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userDAO.existsByEmail(mockRegistrationDTO.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(mockRegistrationDTO));

        verify(userDAO, times(1)).existsByEmail(mockRegistrationDTO.getEmail());
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDAO.findByEmail(mockLoginDTO.getEmail())).thenReturn(mockUser);
        when(jwtService.generateToken(mockLoginDTO.getEmail(), mockLoginDTO.getRole())).thenReturn("mockToken");

        Map<String, String> loginResponse = userService.loginUser(mockLoginDTO);

        assertNotNull(loginResponse);
        assertEquals("mockToken", loginResponse.get("token"));
        assertEquals(mockUser.getUserId(), loginResponse.get("uuid"));
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDAO, times(1)).findByEmail(mockLoginDTO.getEmail());
        verify(jwtService, times(1)).generateToken(mockLoginDTO.getEmail(), mockLoginDTO.getRole());
    }



    @Test
    void testLoginUser_IncorrectRole() {
        LoginDTO incorrectRoleLoginDTO = new LoginDTO();
        incorrectRoleLoginDTO.setEmail("test@example.com");
        incorrectRoleLoginDTO.setPassword("password");
        incorrectRoleLoginDTO.setRole(Roles.ADMIN);

        Authentication authentication = mock(Authentication.class);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDAO.findByEmail("test@example.com")).thenReturn(mockUser);

        assertThrows(UserNotFoundException.class, () -> userService.loginUser(incorrectRoleLoginDTO));

        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDAO, times(1)).findByEmail("test@example.com");
        verify(jwtService, never()).generateToken(anyString(), any());
    }

    @Test
    void testUpdateUserDetails_Success() {
        when(userDAO.findByEmail(mockUpdationDTO.getEmail())).thenReturn(mockUser);
        when(userDAO.save(any(User.class))).thenReturn(mockUser);

        String updateResult = userService.updateUserDetails(mockUpdationDTO);

        assertEquals("Updated Successfully", updateResult);
        verify(userDAO, times(1)).findByEmail(mockUpdationDTO.getEmail());
        verify(userDAO, times(1)).save(any(User.class));
        assertEquals(mockUpdationDTO.getUserName(), mockUser.getUserName());
        assertTrue(encoder.matches(mockUpdationDTO.getPassword(), mockUser.getPassword()));
        assertEquals(mockUpdationDTO.getEmail(), mockUser.getEmail());
    }

    @Test
    void testUpdateUserDetails_UserNotFound() {
        when(userDAO.findByEmail(mockUpdationDTO.getEmail())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.updateUserDetails(mockUpdationDTO));

        verify(userDAO, times(1)).findByEmail(mockUpdationDTO.getEmail());
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    void testGetUsersByIds_Success() {
        List<String> userIds = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        User user1 = new User();
        user1.setUserId(userIds.get(0));
        User user2 = new User();
        user2.setUserId(userIds.get(1));
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userDAO.findAllById(userIds)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersByIds(userIds);

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertTrue(actualUsers.containsAll(expectedUsers));
        verify(userDAO, times(1)).findAllById(userIds);
    }

    @Test
    void testGetUsersByIds_EmptyList() {
        List<String> userIds = Arrays.asList(UUID.randomUUID().toString());
        List<User> expectedUsers = Arrays.asList();
        when(userDAO.findAllById(userIds)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersByIds(userIds);

        assertTrue(actualUsers.isEmpty());
        verify(userDAO, times(1)).findAllById(userIds);
    }

    @Test
    void testGetuserDetails_Success() {
        when(userDAO.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

        User retrievedUser = userService.getuserDetails(mockUser.getUserId());

        assertNotNull(retrievedUser);
        assertEquals(mockUser.getUserId(), retrievedUser.getUserId());
        assertEquals(mockUser.getUserName(), retrievedUser.getUserName());
        assertEquals(mockUser.getEmail(), retrievedUser.getEmail());
        assertEquals(mockUser.getRole(), retrievedUser.getRole());
        verify(userDAO, times(1)).findById(mockUser.getUserId());
    }

    @Test
    void testGetuserDetails_UserNotFound() {
        String nonExistingUserId = UUID.randomUUID().toString();
        when(userDAO.findById(nonExistingUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getuserDetails(nonExistingUserId));

        verify(userDAO, times(1)).findById(nonExistingUserId);
    }

    @Test
    void testGetAllUser_Success() {
        User user2 = new User();
        user2.setUserId(UUID.randomUUID().toString());
        List<User> allUsers = Arrays.asList(mockUser, user2);
        when(userDAO.findAll()).thenReturn(allUsers);

        List<User> retrievedUsers = userService.getAllUser();

        assertEquals(2, retrievedUsers.size());
        assertTrue(retrievedUsers.contains(mockUser));
        assertTrue(retrievedUsers.contains(user2));
        verify(userDAO, times(1)).findAll();
    }

    @Test
    void testGetAllUser_EmptyList() {
        when(userDAO.findAll()).thenReturn(Arrays.asList());

        List<User> retrievedUsers = userService.getAllUser();

        assertTrue(retrievedUsers.isEmpty());
        verify(userDAO, times(1)).findAll();
    }

    @Test
    void testCheckInstructor_IsInstructor() {
        User instructorUser = new User();
        instructorUser.setUserId(UUID.randomUUID().toString());
        instructorUser.setRole(Roles.INSTRUCTOR);
        when(userDAO.findById(instructorUser.getUserId())).thenReturn(Optional.of(instructorUser));

        assertTrue(userService.checkInstructor(instructorUser.getUserId()));
        verify(userDAO, times(1)).findById(instructorUser.getUserId());
    }

    @Test
    void testCheckInstructor_IsNotInstructor() {
        when(userDAO.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

        assertFalse(userService.checkInstructor(mockUser.getUserId()));
        verify(userDAO, times(1)).findById(mockUser.getUserId());
    }

    @Test
    void testCheckInstructor_UserNotFound() {
        String nonExistingUserId = UUID.randomUUID().toString();
        when(userDAO.findById(nonExistingUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.checkInstructor(nonExistingUserId));
        verify(userDAO, times(1)).findById(nonExistingUserId);
    }

    @Test
    void testCheckStudent_IsStudent() {
        when(userDAO.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

        assertTrue(userService.checkStudent(mockUser.getUserId()));
        verify(userDAO, times(1)).findById(mockUser.getUserId());
    }

    @Test
    void testCheckStudent_IsNotStudent() {
        User instructorUser = new User();
        instructorUser.setUserId(UUID.randomUUID().toString());
        instructorUser.setRole(Roles.INSTRUCTOR);
        when(userDAO.findById(instructorUser.getUserId())).thenReturn(Optional.of(instructorUser));

        assertFalse(userService.checkStudent(instructorUser.getUserId()));
        verify(userDAO, times(1)).findById(instructorUser.getUserId());
    }

    @Test
    void testCheckStudent_UserNotFound() {
        String nonExistingUserId = UUID.randomUUID().toString();
        when(userDAO.findById(nonExistingUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.checkStudent(nonExistingUserId));
        verify(userDAO, times(1)).findById(nonExistingUserId);
    }
}
