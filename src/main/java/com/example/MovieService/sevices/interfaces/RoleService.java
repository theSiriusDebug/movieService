package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();

    void saveRole(Role role);

    Role findRoleByName(String roleName);
}
