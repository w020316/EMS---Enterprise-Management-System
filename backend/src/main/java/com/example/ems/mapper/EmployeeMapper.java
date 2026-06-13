package com.example.ems.mapper;

import com.example.ems.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<Employee> findAll();
    List<Employee> findByCondition(@Param("name") String name, @Param("departmentId") Long departmentId, @Param("status") Integer status);
    Employee findById(@Param("id") Long id);
    int insert(Employee employee);
    int update(Employee employee);
    int deleteById(@Param("id") Long id);
    int countByDepartmentId(@Param("departmentId") Long departmentId);
    int countAll();
    int countActive();
}
