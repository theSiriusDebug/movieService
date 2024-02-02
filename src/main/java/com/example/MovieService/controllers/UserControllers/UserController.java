package com.example.MovieService.controllers.UserControllers;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;
import com.example.MovieService.models.dtos.userDtos.EditUserDto;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "UserController")
public class UserController {
    private final UserServiceImpl service;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserController(UserServiceImpl service, BCryptPasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @ApiOperation("Update user")
    @PostMapping("/edit")
    public ResponseEntity<User> updateUser(@RequestBody EditUserDto editUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(updateUser(editUser, service.findByOptionalUsername(auth.getName())));
    }

    private User updateUser(EditUserDto editUser, User user){
        if (isPasswordAndUsernameValid(editUser, user)) {
            if (editUser.getNewUsername() != null) {
                user.setUsername(editUser.getNewUsername());
            }
            if (editUser.getNewPassword() != null) {
                user.setPassword(encoder.encode(editUser.getNewPassword()));
            }
            return service.save(user);
        } else {
            throw new RuntimeException("Wrong password or username");
        }
    }

    private boolean isPasswordAndUsernameValid(EditUserDto editUser, User user) {
        return encoder.matches(editUser.getPassword(), user.getPassword()) && editUser.getUsername().equals(user.getUsername());
    }


    @ApiOperation("Retrieve all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> get(){
        return ResponseEntity.ok(service.findAllUsers());
    }

    @ApiOperation("Get user by id")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") long id){
        return ResponseEntity.ok(service.findUserById(id));
    }
}
