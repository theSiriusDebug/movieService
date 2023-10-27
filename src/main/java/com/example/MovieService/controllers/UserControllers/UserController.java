package com.example.MovieService.controllers.UserControllers;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "UserController")
public class UserController {
    private final UserRepository userRepository;
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

        User existingUser = userRepository.findByUsername(currentUserName);
        if (existingUser != null) {
            return ResponseEntity.ok(existingUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @ApiOperation("Update user")
    @PostMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User existingUser = userRepository.findByUsername(currentUserName);
        if (existingUser != null) {
            existingUser.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

            userRepository.save(existingUser);

            return ResponseEntity.ok(existingUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> get(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> get(@PathVariable("id") long id){
        return ResponseEntity.ok(userRepository.findById(id));
    }
}
