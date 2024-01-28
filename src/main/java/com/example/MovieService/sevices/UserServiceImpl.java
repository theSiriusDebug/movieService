package com.example.MovieService.sevices;

import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;
import com.example.MovieService.repositories.UserRepository;
import com.example.MovieService.sevices.interfaces.UserService;
import com.example.MovieService.utils.mappers.UserMapper;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("User is not found.");
            throw new UsernameNotFoundException("User is not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));

    }

    private Set<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public User findByOptionalUsername(String username) {
        try {
            log.info("Retrieved optional user with username {} ", username);
            return Optional.ofNullable(userRepository.findByUsername(username))
                    .orElseThrow(() -> new NotFoundException("User not found"));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        log.info("Save user with username {} ", user.getUsername());
        return userRepository.save(Objects.requireNonNull(user, "User cannot be null."));
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long id) {
        log.info("Fetching user with ID: " + id);
        try {

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Not found"));
            return UserMapper.mapToUserDto(user);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
