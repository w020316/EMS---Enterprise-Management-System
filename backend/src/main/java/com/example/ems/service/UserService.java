package com.example.ems.service;

import com.example.ems.dto.ChangePasswordDTO;
import com.example.ems.dto.LoginDTO;
import com.example.ems.dto.RegisterDTO;
import com.example.ems.dto.Result;
import com.example.ems.entity.User;
import com.example.ems.mapper.UserMapper;
import com.example.ems.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public Result<?> login(LoginDTO loginDTO) {
        User user = userMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", sanitizeUser(user));

        return Result.success(data);
    }

    public Result<?> register(RegisterDTO registerDTO) {
        User existUser = userMapper.findByUsername(registerDTO.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setRole("user");
        user.setStatus(1);

        userMapper.insert(user);
        return Result.success("注册成功");
    }

    public Result<?> getUserInfo(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(sanitizeUser(user));
    }

    public Result<?> updateProfile(Long userId, User updateData) {
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // Only allow updating safe fields
        if (updateData.getEmail() != null) user.setEmail(updateData.getEmail());
        if (updateData.getPhone() != null) user.setPhone(updateData.getPhone());
        if (updateData.getAvatar() != null) user.setAvatar(updateData.getAvatar());
        userMapper.update(user);
        return Result.success(sanitizeUser(user));
    }

    public Result<?> changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userMapper.findById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return Result.error("旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.update(user);
        return Result.success("密码修改成功");
    }

    private User sanitizeUser(User user) {
        user.setPassword(null);
        return user;
    }
}
