package com.example.MovieService.sevices;

import com.example.MovieService.models.Role;
import com.example.MovieService.models.dtos.userDtos.RoleDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.interfaces.RoleService;
import com.example.MovieService.utils.validator.RoleMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> findAllRoles() {
        log.info("Retrieving all roles");
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::mapToRoleDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveRole(@Valid RoleDto role) {
        roleRepository.save(Objects.requireNonNull(RoleMapper.mapToRole(role), "Role cannot be null."));
        log.info("Role saved successfully.");
    }

    @Override
    public Role findRoleByName(@NotBlank String roleName) {
        return Objects.requireNonNull(roleRepository.findByName(roleName));
    }
}
