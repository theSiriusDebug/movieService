package com.example.MovieService.controllers;

import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ControllerWithTestMethods {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public ControllerWithTestMethods(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/usertest")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/roletest")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping("/roles")
    public String pasteRoles(){
        Role user = new Role("ROLE_USER");
        Role admin = new Role("ROLE_ADMIN");
        roleRepository.save(user);
        roleRepository.save(admin);
        return "successfully";
    }
}
