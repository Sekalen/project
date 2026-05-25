package com.example.final_project.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenProvider {
    String generateAccessToken(UserDetails user);
    String generateRefreshToken(UserDetails user);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}