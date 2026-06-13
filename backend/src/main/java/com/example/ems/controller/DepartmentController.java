package com.example.ems.controller;

import com.example.ems.dto.Result;
import com.example.ems.entity.Department;
import com.example.ems.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Result<?> add(@RequestBody Department department) {
        return departmentService.add(department);
    }

    @PutMapping
    public Result<?> update(@RequestBody Department department) {
        return departmentService.update(department);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return departmentService.deleteById(id);
    }
}
