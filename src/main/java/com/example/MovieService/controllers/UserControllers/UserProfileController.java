package com.example.MovieService.controllers.UserControllers;

import com.example.MovieService.models.dtos.UserRegistrationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "UserProfileController API")
public class UserProfileController {
    @ApiOperation("Get user profile")
    @GetMapping("/profile")
    public UserRegistrationDto getUserProfile() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        return userDto;
    }
}
