package com.tubenotify.tubenotify_backend.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for generating, parsing, and validating JWT tokens
 */
@Component
@Slf4j
public class JwtTokenService {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.accessExpirationInMils}")
    private long accessExpirationMs;

    @Value("${auth.token.refreshExpirationInMils}")
    private long refreshExpirationMs;

    /**
     * Generates a JWT access token for the given user
     *
     * @param userDetails the authenticated user's details
     * @return a signed JWT access token
     */
    public String generateToken(UserDetails userDetails) {

        String userEmail = userDetails.getUsername();

        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return buildToken(userEmail, accessExpirationMs, claims);
    }

    /**
     * Generates a JWT refresh token for the given user
     *
     * @param userDetails the authenticated user's details
     * @return a signed JWT refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {

        String userEmail = userDetails.getUsername();

        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));


        return  buildToken( userEmail, refreshExpirationMs, claims);
    }

    /**
     * Extracts the username (email) from a JWT token
     *
     * @param token the JWT token
     * @return the username (email) stored in the token
     */
    public String getUsernameFromToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a JWT token
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {

            log.error("Invalid JWT token");

            return false;
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the request
     *
     * @param request the incoming HTTP request
     * @return the JWT token string, or null if not present
     */
    public String extractTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            return bearerToken.substring(7);
        }

        return null;
    }

    /**
     * Builds a signed JWT token with the given subject, expiration, and claims
     *
     * @param subject the subject of the token (email)
     * @param expirationMs the token expiration time in milliseconds
     * @param claims additional claims to include in the token
     * @return a signed JWT token
     */
    private String buildToken(String subject, long expirationMs, Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Builds the signing key from the configured JWT secret
     *
     * @return the signing Key
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
