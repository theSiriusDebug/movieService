package com.example.MovieService.controllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.AuthDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private RoleRepository roleRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto authRequest) {
        try {
            String username = authRequest.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));
            User user = userService.findByUsername(username);
            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            System.out.println("good!");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            System.out.println("bad!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

