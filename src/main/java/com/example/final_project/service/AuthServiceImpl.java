package com.example.final_project.service;

import com.example.final_project.dto.LoginRequest;
import com.example.final_project.dto.LoginResponse;
import com.example.final_project.jwt.JwtTokenProvider;
import com.example.final_project.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = tokenProvider.generateAccessToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(accessToken, 3600).toString());
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(refreshToken, 86400).toString());

        LoginResponse response = new LoginResponse(true, userDetails.getAuthorities().iterator().next().getAuthority());
        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().build();
        }
        String username = tokenProvider.getUsernameFromToken(refreshToken);
        // Здесь должна быть загрузка пользователя — упрощённо
        return ResponseEntity.ok(new LoginResponse(true, "ROLE_USER"));
    }

    @Override
    public ResponseEntity<LoginResponse> logout() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie().toString());
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshTokenCookie().toString());
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(false, null));
    }
}