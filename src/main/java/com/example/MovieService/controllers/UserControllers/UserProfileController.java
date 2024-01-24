package com.example.MovieService.controllers.UserControllers;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;
import com.example.MovieService.sevices.UserServiceImpl;
import com.example.MovieService.utils.validator.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "UserProfileController API")
public class UserProfileController {
    private final UserServiceImpl userService;

    @Autowired
    public UserProfileController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ApiOperation("Get user profile")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByOptionalUsername(authentication.getName());

        return ResponseEntity.ok(UserMapper.mapToUserDto(currentUser));
    }
}
