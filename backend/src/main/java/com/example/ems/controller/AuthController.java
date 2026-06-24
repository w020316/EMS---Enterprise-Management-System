package com.example.ems.controller;

import com.example.ems.dto.ChangePasswordDTO;
import com.example.ems.dto.LoginDTO;
import com.example.ems.dto.RegisterDTO;
import com.example.ems.dto.Result;
import com.example.ems.entity.User;
import com.example.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@Validated @RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @GetMapping("/info")
    public Result<?> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.getUserInfo(userId);
    }

    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody User user, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.updateProfile(userId, user);
    }

    @PutMapping("/password")
    public Result<?> changePassword(@Validated @RequestBody ChangePasswordDTO dto, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.changePassword(userId, dto);
    }
}
