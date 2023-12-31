package com.example.MovieService.controllers;

import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Home API")
public class HomeController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public HomeController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @ApiOperation("Get home page")
    @GetMapping("/")
    public String getHomePage() {
        return "homePage";
    }
}
