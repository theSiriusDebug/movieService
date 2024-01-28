package com.example.MovieService.controllers.UserControllers.RoleControllers;

import com.example.MovieService.models.dtos.userDtos.RoleDto;
import com.example.MovieService.sevices.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        RoleDto user = new RoleDto();
        user.setName("ROLE_USER");
        roleServiceImpl.saveRole(user);

        RoleDto admin = new RoleDto();
        admin.setName("ROLE_ADMIN");
        roleServiceImpl.saveRole(admin);

        return "successfully";
    }

    @GetMapping("/roletest")
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok(roleServiceImpl.findAllRoles());
    }
}
