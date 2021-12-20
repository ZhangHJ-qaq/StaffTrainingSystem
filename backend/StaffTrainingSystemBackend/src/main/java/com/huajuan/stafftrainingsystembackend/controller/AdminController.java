package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.contants.SecurityConstants;
import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.entity.Log;
import com.huajuan.stafftrainingsystembackend.http.MyHttpResponseObj;
import com.huajuan.stafftrainingsystembackend.repository.LogRepository;
import com.huajuan.stafftrainingsystembackend.request.admin.ModifyCourseRequest;
import com.huajuan.stafftrainingsystembackend.request.admin.ModifyEmployeeRequest;
import com.huajuan.stafftrainingsystembackend.service.CourseService;
import com.huajuan.stafftrainingsystembackend.service.EmployeeService;
import com.huajuan.stafftrainingsystembackend.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class AdminController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRepository logRepository;

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

    @PostMapping("/admin/modify_employee")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<MyHttpResponseObj> modifyEmployee(
            HttpServletRequest httpReq,
            @Valid @RequestBody ModifyEmployeeRequest modifyEmployeeRequest,
            BindingResult result) {

        //从http头中获得操作者
        String operator = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        if (result.hasFieldErrors()) {
            throw new RuntimeException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        employeeService.modifyEmployee(modifyEmployeeRequest, operator);

        return ResponseEntity.ok(new MyHttpResponseObj(200, "操作成功"));
    }


    /**
     * 获得所有课程的信息
     *
     * @return 课程信息
     */
    @GetMapping("/admin/all_course_info")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Map<String, Object>> allCourseInfo() {
        List<CourseDTO> courseDTOList = courseService.allCourseInfo();
        Map<String, Object> res = new HashMap<>();
        res.put("all_course_info", courseDTOList);
        return ResponseEntity.ok(res);
    }


    /**
     * 修改课程
     *
     * @param httpReq             修改课程的请求
     * @param modifyCourseRequest
     * @param result
     * @return
     */
    @PostMapping("/admin/modify_course")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<MyHttpResponseObj> modifyCourse(
            HttpServletRequest httpReq,
            @Valid @RequestBody ModifyCourseRequest modifyCourseRequest,
            BindingResult result) {

        //从http头中获得操作者
        String operator = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        if (result.hasFieldErrors()) {
            throw new RuntimeException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        courseService.modifyCourse(modifyCourseRequest, operator);

        return ResponseEntity.ok(new MyHttpResponseObj(200, "操作成功"));
    }


    /**
     * 管理员查看全部日志
     */
    @GetMapping("/admin/all_log")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<Log>> allLog() {
        return ResponseEntity.ok(logRepository.findAll());
    }


    @GetMapping("/admin/employee_info")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<EmployeeDTO> employeeInfo(@Param("employeeID") String employeeID, @Param("name") String name) {
        EmployeeDTO employeeDTO;
        if (employeeID != null) { //如果前端有传来员工ID，就按员工ID查询
            employeeDTO = employeeService.findEmployeeDTOWithScoreWithEmployeeID(employeeID);
        } else {
            employeeDTO = employeeService.findEmployeeDTOWithScoreWithName(name);
        }

        return ResponseEntity.ok(employeeDTO);
    }

}
