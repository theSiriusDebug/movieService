package com.example.MovieService.controllers.UserControllers.RoleControllers;

import com.example.MovieService.models.Role;
import com.example.MovieService.sevices.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public RoleController(RoleServiceImpl roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping
    public String pasteRoles(){
        Role user = new Role("ROLE_USER");
        Role admin = new Role("ROLE_ADMIN");
        roleServiceImpl.saveRole(user);
        roleServiceImpl.saveRole(admin);
        return "successfully";
    }

    @GetMapping("/roletest")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleServiceImpl.findAllRoles());
    }
}
