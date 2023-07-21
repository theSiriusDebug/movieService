package com.example.MovieService.controllers;

import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.sevices.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Api(tags = "RegistrationController API")
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("Show registration form")
    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";
    }

    @ApiOperation("Register a user account")
    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) {
        try {
            userService.save(userRegistrationDto, userRegistrationDto.getRole());
            return "redirect:/login";
        } catch (Exception e) {
            return "error";
        }
    }
}
