package com.example.ems.service;

import com.example.ems.dto.Result;
import com.example.ems.entity.Employee;
import com.example.ems.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Result<?> findAll() {
        List<Employee> list = employeeMapper.findAll();
        return Result.success(list);
    }

    public Result<?> findByCondition(String name, Long departmentId, Integer status) {
        List<Employee> list = employeeMapper.findByCondition(name, departmentId, status);
        return Result.success(list);
    }

    public Result<?> findById(Long id) {
        Employee employee = employeeMapper.findById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(employee);
    }

    public Result<?> add(Employee employee) {
        if (employee.getStatus() == null) {
            employee.setStatus(1);
        }
        employeeMapper.insert(employee);
        return Result.success("添加成功");
    }

    public Result<?> update(Employee employee) {
        Employee exist = employeeMapper.findById(employee.getId());
        if (exist == null) {
            return Result.error("员工不存在");
        }
        employeeMapper.update(employee);
        return Result.success("更新成功");
    }

    public Result<?> deleteById(Long id) {
        employeeMapper.deleteById(id);
        return Result.success("删除成功");
    }

    public Result<?> getStats() {
        int total = employeeMapper.countAll();
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalEmployees", total);
        return Result.success(stats);
    }
}
