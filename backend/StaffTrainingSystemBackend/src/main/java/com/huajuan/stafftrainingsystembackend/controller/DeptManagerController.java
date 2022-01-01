package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.contants.SecurityConstants;
import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.dto.departmentmanager.TransferQualificationDTO;
import com.huajuan.stafftrainingsystembackend.http.MyHttpResponseObj;
import com.huajuan.stafftrainingsystembackend.request.departmentmanager.AllocateCourseRequest;
import com.huajuan.stafftrainingsystembackend.request.departmentmanager.TransferDepartmentRequest;
import com.huajuan.stafftrainingsystembackend.service.CourseService;
import com.huajuan.stafftrainingsystembackend.service.EmployeeService;
import com.huajuan.stafftrainingsystembackend.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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


    /**
     * 部门经理给学员分配课程
     *
     * @return
     */
    @PostMapping("/department_manager/allocate_course")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<MyHttpResponseObj> allocateCourse(
            HttpServletRequest httpReq,
            @Valid @RequestBody AllocateCourseRequest allocateCourseRequest,
            BindingResult result
    ) {
        //从http头中获得操作者
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        if (result.hasFieldErrors()) {
            throw new RuntimeException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //在业务层分配课程
        courseService.allocateCourse(allocateCourseRequest, deptManagerID);

        return ResponseEntity.ok(new MyHttpResponseObj(HttpServletResponse.SC_OK, "操作成功"));

    }


    /**
     * 部门经理根据员工ID或姓名，查询这个部门下对应的员工
     *
     * @param employeeID 员工编号
     * @param name       姓名
     * @return 员工信息（DTO）
     */
    @GetMapping("/department_manager/dept_employee_info")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<EmployeeDTO> deptEmployeeInfo(
            HttpServletRequest httpReq,
            @Param("employeeID") String employeeID,
            @Param("name") String name) {

        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        EmployeeDTO employeeDTO = null;
        if (employeeID != null && !employeeID.isEmpty()) {
            employeeDTO = employeeService.findEmployeeDTOWithScoreWithEmployeeID(employeeID);
        } else if (name != null) {
            employeeDTO = employeeService.findEmployeeDTOWithScoreWithName(name);
        }

        if (!employeeService.deptManagerAndEmployeeInSameDepartment(deptManagerID, employeeID)) {
            throw new RuntimeException("不能查询不属于自己部门的员工的信息");
        }

        if (employeeDTO == null) {
            throw new RuntimeException("找不到员工");
        }

        return ResponseEntity.ok(employeeDTO);
    }


    /**
     * 部门经理查询自己员工下的部门是否符合转部门要求
     *
     * @return TransferQualification的列表
     */
    @GetMapping("/department_manager/check_all_transfer")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<List<TransferQualificationDTO>> checkAllTransfer(HttpServletRequest httpReq) {

        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        return
                ResponseEntity.ok(employeeService.getTransferQualificationOfAllEmployeeInMyDepartment(deptManagerID));

    }


    /**
     * 部门经理根据员工姓名或者员工编号查询员工是否符合转部门要求
     *
     * @param employeeID 员工ID
     * @param name       姓名
     * @return 员工的transferQualification
     */
    @GetMapping("/department_manager/check_transfer")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<TransferQualificationDTO> checkTransfer(
            HttpServletRequest httpReq,
            @Param("employeeID") String employeeID,
            @Param("name") String name
    ) {
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        //如果没有传入employeeID而传入的是name，就根据name查找employeeID
        if (employeeID == null || employeeID.isEmpty()) {
            employeeID = employeeService.findUniqueEmployeeIDByName(name);
        }

        return
                ResponseEntity.ok(
                        employeeService.getTransferQualificationOfOneEmployeeInMyDepartment(deptManagerID, employeeID)
                );
    }


    /**
     * 根据员工的ID或名字，查询他转入新部门以后要培训的课程
     *
     * @param employeeID 员工ID
     * @param name       员工名字
     * @param newDeptID  新的部门的编号
     * @return 转入新部门后要培训的课程
     */
    @GetMapping("/department_manager/courses_after_transfer")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<List<CourseDTO>> coursesAfterTransfer(
            HttpServletRequest httpReq,
            @Param("employeeID") String employeeID,
            @Param("name") String name,
            @RequestParam(value = "newDeptID") Integer newDeptID
    ) {
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        //如果没有传入employeeID而传入的是name，就根据name查找employeeID
        if (employeeID == null || employeeID.isEmpty()) {
            employeeID = employeeService.findUniqueEmployeeIDByName(name);
        }

        return ResponseEntity.ok(courseService.getCoursesAfterTransfer(deptManagerID, employeeID, newDeptID));

    }


    /**
     * 部门经理给自己部门下的员工转部门，根据员工的姓名或编号
     */
    @PostMapping("/department_manager/transfer_department")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<MyHttpResponseObj> transferDepartment(HttpServletRequest httpReq,
                                                                @RequestBody @Valid TransferDepartmentRequest transferDepartmentRequest,
                                                                BindingResult result) {
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        if (result.hasFieldErrors()) {
            throw new RuntimeException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //转部门
        employeeService.transferDepartment(deptManagerID, transferDepartmentRequest);

        return ResponseEntity.ok(
                new MyHttpResponseObj(HttpServletResponse.SC_OK, "转部门成功")
        );

    }

    /**
     * 部门经理获得部门下所有员工的信息，只看通过的成绩
     *
     * @return 部门下所有员工的信息
     */
    @GetMapping("/department_manager/all_dept_employee_info_passed_only")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<List<EmployeeDTO>> allDeptEmployeeInfoPassedOnly(HttpServletRequest httpReq) {
        //从http头中获得部门经理的id
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        return ResponseEntity.ok(
                employeeService.allEmployeeInfoInMyDepartmentPassedOnly(deptManagerID)
        );
    }

    /**
     * 部门经理获得部门下所有员工的信息，只看某一门课的成绩
     *
     * @return 部门下所有员工的信息
     */
    @GetMapping("/department_manager/all_dept_employee_info_course_id_only")
    @PreAuthorize("hasAuthority('department_manager')")
    public ResponseEntity<List<EmployeeDTO>> allDeptEmployeeInfoPassedOnly(HttpServletRequest httpReq, @RequestParam("courseID") String courseID) {
        //从http头中获得部门经理的id
        String deptManagerID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        return ResponseEntity.ok(
                employeeService.allEmployeeInfoInMyDepartmentCourseIDOnly(deptManagerID, courseID)
        );
    }


}
