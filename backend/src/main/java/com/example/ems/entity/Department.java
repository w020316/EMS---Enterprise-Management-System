package com.example.ems.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Department {
    private Long id;
    private String name;
    private String description;
    private String manager;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
