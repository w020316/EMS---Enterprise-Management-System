package com.example.ems.controller;

import com.example.ems.dto.Result;
import com.example.ems.entity.Employee;
import com.example.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) String name,
                          @RequestParam(required = false) Long departmentId,
                          @RequestParam(required = false) Integer status) {
        if (name != null || departmentId != null || status != null) {
            return employeeService.findByCondition(name, departmentId, status);
        }
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public Result<?> add(@RequestBody Employee employee) {
        return employeeService.add(employee);
    }

    @PutMapping
    public Result<?> update(@RequestBody Employee employee) {
        return employeeService.update(employee);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return employeeService.deleteById(id);
    }

    @GetMapping("/stats")
    public Result<?> stats() {
        return employeeService.getStats();
    }
}
