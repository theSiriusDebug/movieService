package com.example.MovieService.controllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.AuthDto;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// AuthController.java
@RestController
@RequestMapping("/api/auth")
public class RegistrationController {
    private AuthenticationManager authenticationManager;
    private UserServiceImpl userService;
    private JwtTokenProvider jwtTokenProvider;
    private RoleRepository roleRepository;

    @Autowired
    public RegistrationController(AuthenticationManager authenticationManager, UserServiceImpl userService, JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto authRequest) {
        try {
            String username = authRequest.getUsername();
            String login = authRequest.getLogin();
            if (userService.findByUsername(username) == null) {
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setLogin(login);
                newUser.setPassword(new BCryptPasswordEncoder().encode(authRequest.getPassword()));
                Role role = roleRepository.findByName("ROLE_USER");
                newUser.setRoles(Collections.singleton(role));
                userService.save(newUser);
                return ResponseEntity.ok("Registration successful");
            } else {
                return ResponseEntity.badRequest().body("Username already exists");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
