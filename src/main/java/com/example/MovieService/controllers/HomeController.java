package com.example.MovieService.controllers;

import com.example.MovieService.repositories.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage() {
        return "homePage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
