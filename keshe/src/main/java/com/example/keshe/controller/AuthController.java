package com.example.keshe.controller;

import com.example.keshe.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.keshe.response.AuthResponse;
import org.springframework.web.bind.annotation.*;
import com.example.keshe.entity.User;
import java.util.Map;



@RestController
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String password = request.get("password");
        String role = request.get("role");
        return authService.login(phone, password, role);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody Map<String, Object> request) {
        User user = new User();
        user.setUsername((String) request.get("username"));
        user.setPassword((String) request.get("password"));
        user.setPhone((String) request.get("phone"));
        user.setRole((String) request.get("role"));
        String confirmPassword = (String) request.get("confirmPassword");

        return authService.register(user, confirmPassword);
    }
}
