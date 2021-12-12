package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 获得所有员工的信息
     *
     * @return
     */
    @GetMapping("/admin/all_employee_info")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Map<String, Object>> allEmployeeInfo() {
        List<EmployeeDTO> employeeDTOList = employeeService.allEmployeeInfo();
        Map<String, Object> res = new HashMap<>();
        res.put("all_employee_info", employeeDTOList);
        return ResponseEntity.ok(res);
    }
}
