package com.example.MovieService.controllers.UserControllers;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.sevices.UserServiceImpl;
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
    private final UserServiceImpl userServiceImpl;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, BCryptPasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation("Edit user")
    @GetMapping("/edit")
    public ResponseEntity<?> updateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User existingUser = userServiceImpl.findByOptionalUsername(currentUserName);
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

        User existingUser = userServiceImpl.findByOptionalUsername(currentUserName);
        if (existingUser != null) {
            existingUser.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

            userServiceImpl.save(existingUser);

            return ResponseEntity.ok(existingUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> get(){
        return ResponseEntity.ok(userServiceImpl.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> get(@PathVariable("id") long id){
        return ResponseEntity.ok(Optional.ofNullable(userServiceImpl.getUserById(id)));
    }
}
