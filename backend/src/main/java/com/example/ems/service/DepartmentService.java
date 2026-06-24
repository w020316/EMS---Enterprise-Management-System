package com.example.ems.service;

import com.example.ems.dto.Result;
import com.example.ems.entity.Department;
import com.example.ems.mapper.DepartmentMapper;
import com.example.ems.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public Result<?> findAll() {
        List<Department> list = departmentMapper.findAll();
        return Result.success(list);
    }

    public Result<?> findById(Long id) {
        Department department = departmentMapper.findById(id);
        if (department == null) {
            return Result.error("部门不存在");
        }
        return Result.success(department);
    }

    public Result<?> add(Department department) {
        if (department.getName() != null) {
            Department existing = departmentMapper.findByName(department.getName());
            if (existing != null) {
                return Result.error("部门名称已存在");
            }
        }
        departmentMapper.insert(department);
        return Result.success("添加成功");
    }

    public Result<?> update(Department department) {
        if (department.getId() == null) {
            return Result.error("部门ID不能为空");
        }
        Department exist = departmentMapper.findById(department.getId());
        if (exist == null) {
            return Result.error("部门不存在");
        }
        if (department.getName() != null) {
            Department existing = departmentMapper.findByName(department.getName());
            if (existing != null && !existing.getId().equals(department.getId())) {
                return Result.error("部门名称已存在");
            }
        }
        departmentMapper.update(department);
        return Result.success("更新成功");
    }

    public Result<?> deleteById(Long id) {
        Department department = departmentMapper.findById(id);
        if (department == null) {
            return Result.error("部门不存在");
        }
        int count = employeeMapper.countByDepartmentId(id);
        if (count > 0) {
            return Result.error("该部门下还有员工，无法删除");
        }
        departmentMapper.deleteById(id);
        return Result.success("删除成功");
    }
}
