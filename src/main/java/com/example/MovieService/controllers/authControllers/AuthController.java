package com.example.MovieService.controllers.authControllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.AuthDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "Login Controller")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    @Value("${jwt.refreshTokenValidityInMilliseconds}")
    private long refreshTokenValidityInMilliseconds;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto authRequest, HttpServletResponse response) {
        try {
            String username = authRequest.getUsername();
            authenticateUser(username, authRequest.getPassword());

            User user = userService.findByUsername(username);
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            Set<Role>roles = user.getRoles();

            String refreshToken = jwtTokenProvider.createRefreshToken(username, user.getRoles());
            setRefreshTokenCookie(response, refreshToken);

            Map<Object, Object> responseBody = createResponse(username, token, roles);

            logger.info("Login successful for user: {}", username);
            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            logger.error("Login failed for user: {}", authRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("An error occurred during login for user: {}", authRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void authenticateUser(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (refreshTokenValidityInMilliseconds / 1000));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private Map<Object, Object> createResponse(String username, String token, Set<Role> roles) {
        Map<Object, Object> responseBody = new HashMap<>();
        responseBody.put("username", username);
        responseBody.put("token", token);
        responseBody.put("roles", roles);
        return responseBody;
    }
}
