package com.example.final_project.service;

import com.example.final_project.dto.LoginRequest;
import com.example.final_project.dto.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest request);
    ResponseEntity<LoginResponse> refresh(String refreshToken);
    ResponseEntity<LoginResponse> logout();
}