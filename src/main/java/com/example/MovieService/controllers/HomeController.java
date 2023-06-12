package com.example.MovieService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "homePage"; // Возвращает имя шаблона домашней страницы (например, home.html)
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
