package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.contants.SecurityConstants;
import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.service.CourseService;
import com.huajuan.stafftrainingsystembackend.service.EmployeeService;
import com.huajuan.stafftrainingsystembackend.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class DeptManagerController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CourseService courseService;

    /**
     * 部门经理获得部门下所有员工的信息
     *
     * @return 部门下所有员工的信息
     */
    @GetMapping("/department_manager/all_dept_employee_info")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<List<EmployeeDTO>> allDeptEmployeeInfo(HttpServletRequest httpReq) {
        //从http头中获得部门经理的id
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        return
                ResponseEntity.ok(employeeService.allEmployeeInfoInMyDepartment(deptManagerID));
    }

    /**
     * 部门经理获得部门下所有课程的信息
     *
     * @return 部门下所有课程的信息
     */
    @GetMapping("/department_manager/dept_course_info")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<List<CourseDTO>> deptCourseInfo(HttpServletRequest httpReq) {
        //从http头中获得部门经理的id
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        return
                ResponseEntity.ok(courseService.allCourseInfoInMyDepartment(deptManagerID));
    }
}
