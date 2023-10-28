package com.example.MovieService.jwt;

import com.example.MovieService.models.Role;
import com.example.MovieService.sevices.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.refreshSecretKey}")
    private String refreshSecretKey;
    @Value("${jwt.tokenValidityInMilliseconds}")
    private long tokenValidityInMilliseconds;
    @Value("${jwt.refreshTokenValidityInMilliseconds}")
    private long refreshTokenValidityInMilliseconds;

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final String secretKey = "QjSLlIwwQaCdw2jLZy/x9N5me0LyUUd9GsNFcGLCDlU=";

    private final UserService userService;

    @Autowired
    public JwtTokenProvider(UserService userService) {
        this.userService = userService;
    }

    private final long validityInMilliseconds = 3600000;

    public String createToken(String username, Collection<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getName())).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        logger.info("Created JWT Token for user: {}", username);
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String username, Collection<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getName())).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        logger.info("Created Refresh Token for user: {}", username);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);

        logger.info("Created authentication object for user: {}", username);

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Set<SimpleGrantedAuthority> getAuthorities(String token) {
        List<Map<String, String>> roleClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("auth", List.class);
        Set<SimpleGrantedAuthority> authorities = roleClaims.stream()
                .map(roleClaim -> new SimpleGrantedAuthority(roleClaim.get("authority")))
                .collect(Collectors.toSet());

        logger.info("Extracted authorities from JWT Token: {}", authorities);

        return authorities;
        return roleClaims.stream().map(roleClaim -> new SimpleGrantedAuthority(roleClaim.get("authority"))).collect(Collectors.toSet());
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (!claims.getBody().getExpiration().before(new Date())) {
                logger.info("Valid JWT Token received for user: {}", getUsername(token));
                return true;
            } else {
                logger.warn("Expired JWT Token received for user: {}", getUsername(token));
                return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT Token validation failed.", e);
            return false;
        }
    }

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
