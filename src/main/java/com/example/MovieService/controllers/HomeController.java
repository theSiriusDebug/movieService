package com.example.MovieService.controllers;

import com.example.MovieService.sevices.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Home API")
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("Get home page")
    @GetMapping("/")
    public String getHomePage() {
        return "homePage";
    }
}
