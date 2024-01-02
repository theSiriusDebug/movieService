package com.example.MovieService.sevices;

import com.example.MovieService.models.Role;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.interfaces.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRoles() {
        log.info("Retrieving all roles");
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(Objects.requireNonNull(role, "Role cannot be null."));
        log.info("Role saved successfully.");
    }

    @Override
    public Role findRoleByName(String roleName) {
        return Objects.requireNonNull(roleRepository.findByName(roleName));
    }
}
