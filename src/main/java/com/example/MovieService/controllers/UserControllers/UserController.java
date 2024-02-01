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
    public ResponseEntity<?> updateUser(@RequestBody EditUserDto editUserDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = service.findByOptionalUsername(auth.getName());
        updateUser(editUserDto, user);
        return ResponseEntity.ok(service.save(user));
    }

    private void updateUser(EditUserDto editUser, User user){
        String currentPassword = user.getPassword();
        String currentUsername = user.getUsername();

        String editUserPassword = editUser.getPassword();
        String editUserUsername = editUser.getUsername();

        String newPassword = editUser.getNew_password();
        String newUsername = editUser.getNew_username();

        if (encoder.matches(editUserPassword, currentPassword) && editUserUsername.equals(currentUsername)) {
            if (newUsername != null) {
                user.setUsername(newUsername);
            }
            if (newPassword != null) {
                user.setPassword(encoder.encode(newPassword));
            }
        }
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
