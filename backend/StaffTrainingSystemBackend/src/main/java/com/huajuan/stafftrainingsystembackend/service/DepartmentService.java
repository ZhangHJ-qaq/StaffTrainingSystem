package com.huajuan.stafftrainingsystembackend.service;

import com.huajuan.stafftrainingsystembackend.entity.Department;
import com.huajuan.stafftrainingsystembackend.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    /**
     * 获得所有部门的信息
     *
     * @return
     */
    public List<Department> all_department_info() {
        return departmentRepository.findAll();
    }
}
