package com.example.MovieService.sevices;

import com.example.MovieService.models.Role;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private static final Logger logger = Logger.getLogger(ReviewServiceImpl.class.getName());

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRoles() {
        logger.info("Retrieving all roles");
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(Role role){
        roleRepository.save(Objects.requireNonNull(role, "Role cannot be null."));
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
