package com.example.ems.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Employee {
    private Long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private Long departmentId;
    private String position;
    private BigDecimal salary;
    private LocalDate hireDate;
    private Integer status;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 非数据库字段
    private String departmentName;
}
