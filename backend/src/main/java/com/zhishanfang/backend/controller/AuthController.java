package com.zhishanfang.backend.controller;

import com.zhishanfang.backend.dto.AuthRequest;
import com.zhishanfang.backend.dto.UserResponse;
import com.zhishanfang.backend.service.AuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }

    @PostMapping("/guest")
    public UserResponse guest() {
        return authService.guest();
    }
}
