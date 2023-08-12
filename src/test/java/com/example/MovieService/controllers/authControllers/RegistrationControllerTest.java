package com.example.MovieService.controllers.authControllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
//Created by CHATGPT
public class RegistrationControllerTest {
    private RegistrationController registrationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registrationController = new RegistrationController(authenticationManager, userService, jwtTokenProvider, roleRepository);
    }

    @Test
    public void testRegisterNewUser() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("newUser");
        registrationDto.setPassword("password");

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        when(userService.findByUsername("newUser")).thenReturn(null);

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword(new BCryptPasswordEncoder().encode("password"));
        newUser.setRoles(Collections.singleton(role));
        when(userService.save(any(User.class))).thenReturn(newUser);

        when(jwtTokenProvider.createToken("newUser", newUser.getRoles())).thenReturn("dummyToken");

        ResponseEntity<?> responseEntity = registrationController.register(registrationDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<Object, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("username", "newUser");
        expectedResponse.put("token", "dummyToken");

        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testRegisterExistingUser() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("existingUser");
        registrationDto.setPassword("password");

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        User existingUser = new User();
        existingUser.setUsername("existingUser");
        when(userService.findByUsername("existingUser")).thenReturn(existingUser);

        ResponseEntity<?> responseEntity = registrationController.register(registrationDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Username already exists", responseEntity.getBody());
    }
}