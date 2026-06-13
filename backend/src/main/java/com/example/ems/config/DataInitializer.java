package com.example.ems.config;

import com.example.ems.entity.User;
import com.example.ems.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 初始化管理员账号
        User admin = userMapper.findByUsername("admin");
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@ems.com");
            admin.setRole("admin");
            admin.setStatus(1);
            userMapper.insert(admin);
            System.out.println(">>> 默认管理员账号已创建: admin / admin123");
        }
    }
}
