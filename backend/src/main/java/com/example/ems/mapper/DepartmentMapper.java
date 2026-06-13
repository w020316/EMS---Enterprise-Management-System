package com.example.ems.mapper;

import com.example.ems.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<Department> findAll();
    Department findById(@Param("id") Long id);
    int insert(Department department);
    int update(Department department);
    int deleteById(@Param("id") Long id);
    int countByManager(@Param("manager") String manager);
}
