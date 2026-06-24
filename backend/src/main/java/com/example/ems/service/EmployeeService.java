package com.example.ems.service;

import com.example.ems.dto.Result;
import com.example.ems.entity.Employee;
import com.example.ems.mapper.DepartmentMapper;
import com.example.ems.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    public Result<?> findAll(Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Employee> list = employeeMapper.findAll(offset, size);
        int total = employeeMapper.countAll();
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return Result.success(result);
    }

    public Result<?> findByCondition(String name, Long departmentId, Integer status, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Employee> list = employeeMapper.findByCondition(name, departmentId, status, offset, size);
        int total = employeeMapper.countByCondition(name, departmentId, status);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return Result.success(result);
    }

    public Result<?> findById(Long id) {
        Employee employee = employeeMapper.findById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(employee);
    }

    public Result<?> add(Employee employee) {
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            return Result.error("员工姓名不能为空");
        }
        if (employee.getDepartmentId() != null && departmentMapper.findById(employee.getDepartmentId()) == null) {
            return Result.error("部门不存在");
        }
        if (employee.getStatus() == null) {
            employee.setStatus(1);
        }
        employeeMapper.insert(employee);
        return Result.success("添加成功");
    }

    public Result<?> update(Employee employee) {
        if (employee.getId() == null) {
            return Result.error("员工ID不能为空");
        }
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            return Result.error("员工姓名不能为空");
        }
        if (employee.getDepartmentId() != null && departmentMapper.findById(employee.getDepartmentId()) == null) {
            return Result.error("部门不存在");
        }
        Employee existing = employeeMapper.findById(employee.getId());
        if (existing == null) {
            return Result.error("员工不存在");
        }
        employeeMapper.update(employee);
        return Result.success("更新成功");
    }

    public Result<?> deleteById(Long id) {
        Employee employee = employeeMapper.findById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        employeeMapper.deleteById(id);
        return Result.success("删除成功");
    }

    public Result<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmployees", employeeMapper.countAll());
        stats.put("activeEmployees", employeeMapper.countActive());
        return Result.success(stats);
    }
}
