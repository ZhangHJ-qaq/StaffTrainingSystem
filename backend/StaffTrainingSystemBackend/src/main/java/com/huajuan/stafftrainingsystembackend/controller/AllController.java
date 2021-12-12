package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.entity.Department;
import com.huajuan.stafftrainingsystembackend.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AllController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获得所有部门的信息
     *
     * @return
     */
    @GetMapping("/all/all_department_info")
    public ResponseEntity<Map<String, Object>> all_department_info() {
        List<Department> allDepartments = departmentService.all_department_info();
        Map<String, Object> res = new HashMap<>();
        res.put("all_employee_info", allDepartments);
        return ResponseEntity.ok(res);

    }
}
