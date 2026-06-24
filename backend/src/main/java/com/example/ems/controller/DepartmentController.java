package com.example.ems.controller;

import com.example.ems.dto.Result;
import com.example.ems.entity.Department;
import com.example.ems.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public Result<?> list() {
        return departmentService.findAll();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return departmentService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody Department department) {
        return departmentService.add(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody Department department) {
        return departmentService.update(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return departmentService.deleteById(id);
    }
}
