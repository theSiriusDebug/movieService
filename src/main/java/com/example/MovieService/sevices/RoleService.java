package com.example.MovieService.sevices;

import com.example.MovieService.models.Role;
import com.example.MovieService.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAllRoles() {
        logger.info("Retrieving all roles");
        return roleRepository.findAll();
    }

    public void saveRole(Role role){
        roleRepository.save(role);
    }

    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
