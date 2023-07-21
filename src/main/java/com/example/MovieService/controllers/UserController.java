package com.example.MovieService.controllers;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Api(tags = "UserController")
public class UserController {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation("Edit user")
    @GetMapping("/edit")
    public ResponseEntity<?> updateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Optional<User> existingUserOptional = userRepository.findByUsername(currentUserName);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            return ResponseEntity.ok(existingUser);
        }
        User existingUser = existingUserOptional.get();
        return ResponseEntity.ok(existingUser);
    }

    @ApiOperation("Update user")
    @PostMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Optional<User> existingUserOptional = userRepository.findByUsername(currentUserName);
        if (!existingUserOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User existingUser = existingUserOptional.get();

        existingUser.setLogin(userRegistrationDto.getLogin());
        existingUser.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }

}
