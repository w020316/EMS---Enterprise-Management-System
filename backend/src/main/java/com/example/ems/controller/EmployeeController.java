package com.example.ems.controller;

import com.example.ems.dto.Result;
import com.example.ems.entity.Employee;
import com.example.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) String name,
                          @RequestParam(required = false) Long departmentId,
                          @RequestParam(required = false) Integer status,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (name != null || departmentId != null || status != null) {
            return employeeService.findByCondition(name, departmentId, status, page, size);
        }
        return employeeService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody Employee employee) {
        return employeeService.add(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody Employee employee) {
        return employeeService.update(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return employeeService.deleteById(id);
    }

    @GetMapping("/stats")
    public Result<?> stats() {
        return employeeService.getStats();
    }
}
