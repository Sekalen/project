package com.example.final_project.controller;

import com.example.final_project.dto.LoginRequest;
import com.example.final_project.dto.LoginResponse;
import com.example.final_project.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue(name = "refresh_token") String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout() {
        return authService.logout();
    }
}