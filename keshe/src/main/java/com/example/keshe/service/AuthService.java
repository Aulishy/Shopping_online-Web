package com.example.keshe.service;

import com.example.keshe.entity.User;
import com.example.keshe.mapper.AuthMapper;
import com.example.keshe.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private AuthMapper authMapper;

    // 登录服务
    public AuthResponse login(String phone, String password, String role) {
        User user = authMapper.findByPhoneAndPasswordAndRole(phone, password, role);
        if (user != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("phone", user.getPhone());
            userData.put("role", user.getRole());
            return AuthResponse.success(userData);
        } else {
            return AuthResponse.fail(401, "用户名或密码错误");
        }
    }

    // 注册服务
    public AuthResponse register(User user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) {
            return AuthResponse.fail(400, "两次输入的密码不一致");
        }
        if (authMapper.existsByPhone(user.getPhone())) {
            return AuthResponse.fail(400, "该手机号已注册");
        }
        int result = authMapper.insertUser(user);
        if (result > 0) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("phone", user.getPhone());
            userData.put("role", user.getRole());
            return AuthResponse.success(userData);
        } else {
            return AuthResponse.fail(500, "注册失败");
        }
    }
}