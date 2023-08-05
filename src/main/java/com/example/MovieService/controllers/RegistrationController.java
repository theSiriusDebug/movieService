package com.example.MovieService.controllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "RegistrationController API")
@CrossOrigin(origins = "http://localhost:3000") // Add the front-end URL here
public class RegistrationController {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private RoleRepository roleRepository;

    @Autowired
    public RegistrationController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            String username = registrationDto.getUsername();
            if (userService.findByUsername(username) == null) {
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(new BCryptPasswordEncoder().encode(registrationDto.getPassword()));
                Role role = roleRepository.findByName("ROLE_USER");
                newUser.setRoles(Collections.singleton(role));
                userService.save(newUser);

                String token = jwtTokenProvider.createToken(newUser.getUsername(), newUser.getRoles());

                Map<Object, Object> response = new HashMap<>();
                response.put("username", newUser.getUsername());
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Username already exists");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
