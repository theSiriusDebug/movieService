package com.example.MovieService.sevices;

import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;
import com.example.MovieService.models.dtos.userDtos.EditUserDto;
import com.example.MovieService.repositories.UserRepository;
import com.example.MovieService.sevices.interfaces.UserService;
import com.example.MovieService.utils.mappers.UserMapper;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
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
    public User findByUsername(String username) {
        log.info("Retrieved user with username {} ", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        log.info("Save user with username {} ", user.getUsername());
        return userRepository.save(Objects.requireNonNull(user, "User cannot be null."));
    }

    @Override
    public User updateUser(EditUserDto editUser, User user) {
        log.info("Updating user with ID {} ", user.getId());
        if (isPasswordAndUsernameValid(editUser, user)) {
            if (editUser.getNewUsername() != null) {
                log.info("Updating username");
                user.setUsername(editUser.getNewUsername());
            }
            if (editUser.getNewPassword() != null) {
                log.info("Updating password");
                user.setPassword(encoder.encode(editUser.getNewPassword()));
            }
            log.info("User updated successfully");
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Wrong password or username");
        }
    }

    private boolean isPasswordAndUsernameValid(EditUserDto editUser, User user) {
        log.info("Checking if password and username are valid");
        return encoder.matches(editUser.getPassword(), user.getPassword()) && editUser.getUsername().equals(user.getUsername());
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
