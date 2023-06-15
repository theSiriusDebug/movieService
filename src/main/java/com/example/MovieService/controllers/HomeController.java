package com.example.MovieService.controllers;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String homePage() {
        return "homePage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/edit")
    public String editUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Optional<User> existingUserOptional = userRepository.findByUsername(currentUserName);
        if (existingUserOptional.isEmpty()) {
            // Обработка ошибки, если пользователь не найден
            return "error";
        }
        User existingUser = existingUserOptional.get();

        model.addAttribute("existingUser", existingUser);
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());

        return "edit";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Optional<User> existingUserOptional = userRepository.findByUsername(currentUserName);
        if (existingUserOptional.isEmpty()) {
            // Обработка ошибки, если пользователь не найден
            return "error";
        }
        User existingUser = existingUserOptional.get();

        existingUser.setLogin(userRegistrationDto.getLogin());
        existingUser.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        userRepository.save(existingUser);

        model.addAttribute("existingUser", existingUser);

        return "redirect:/home";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }
}
