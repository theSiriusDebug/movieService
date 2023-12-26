package com.example.MovieService.controllers.UserControllers.RoleControllers;

import com.example.MovieService.models.Role;
import com.example.MovieService.sevices.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String pasteRoles(){
        Role user = new Role("ROLE_USER");
        Role admin = new Role("ROLE_ADMIN");
        roleService.saveRole(user);
        roleService.saveRole(admin);
        return "successfully";
    }

    @GetMapping("/roletest")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }
}
