package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.contants.SecurityConstants;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.dto.ScoreDTO;
import com.huajuan.stafftrainingsystembackend.http.MyHttpResponseObj;
import com.huajuan.stafftrainingsystembackend.repository.ParticipateRepository;
import com.huajuan.stafftrainingsystembackend.request.student.StudentModifySelfInfoRequest;
import com.huajuan.stafftrainingsystembackend.service.EmployeeService;
import com.huajuan.stafftrainingsystembackend.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class StudentController {

    @Autowired
    EmployeeService employeeService;


    /**
     * 学生获得自己的个人信息
     *
     * @param httpReq
     * @return
     */
    @GetMapping("/student/my_info")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<EmployeeDTO> my_info(HttpServletRequest httpReq) {
        //从http头中获得操作者
        String operator = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        EmployeeDTO me = employeeService.findEmployeeByEmployeeIDWithoutScore(operator);

        if (me == null) {
            throw new RuntimeException("找不到用户");
        }

        return ResponseEntity.ok(me);

    }


    @PostMapping("/student/modify_my_info")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<MyHttpResponseObj> modifyMyInfo(
            HttpServletRequest httpReq,
            @RequestBody @Valid StudentModifySelfInfoRequest studentModifySelfInfoRequest,
            BindingResult result
    ) {
        //从http头中获得操作者
        String employeeID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        if (result.hasFieldErrors()) {
            throw new RuntimeException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        employeeService.modifyStudent(employeeID, studentModifySelfInfoRequest);

        return ResponseEntity.ok(
                new MyHttpResponseObj(HttpServletResponse.SC_OK, "操作成功"));

    }


    @GetMapping("/student/my_current_courses")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<List<ScoreDTO>> myCurrentCourses(HttpServletRequest httpReq) {
        //从http头中获得操作者
        String employeeID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        return ResponseEntity.ok(
                employeeService.findCurrentCourses(employeeID)
        );
    }

    @GetMapping("/student/my_history_courses")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<List<ScoreDTO>> myHistoryCourses(HttpServletRequest httpReq) {
        //从http头中获得操作者
        String employeeID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        return ResponseEntity.ok(
                employeeService.findHistoryCourses(employeeID)
        );
    }

}
