package com.example.MovieService.controllers.authControllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.AuthDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Created by CHATGPT
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private HttpServletResponse response;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, userService, jwtTokenProvider, roleRepository);
    }

    @Test
    void testSuccessfulLogin() {
        String username = "testUser";
        String password = "testPassword";
        AuthDto authDto = new AuthDto(username, password);

        User user = new User();
        user.setUsername(username);
        when(userService.findByUsername(username)).thenReturn(user);
        when(jwtTokenProvider.createToken(username, user.getRoles())).thenReturn("testToken");
        when(jwtTokenProvider.createRefreshToken(username, user.getRoles())).thenReturn("testRefreshToken");

        ResponseEntity<?> responseEntity = authController.login(authDto, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testFailedLogin() {
        String username = "testUser";
        String password = "testPassword";
        AuthDto authDto = new AuthDto(username, password);

        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Test Exception") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        });

        ResponseEntity<?> responseEntity = authController.login(authDto, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}
