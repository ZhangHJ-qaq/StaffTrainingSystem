package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.contants.SecurityConstants;
import com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtCourseDTO;
import com.huajuan.stafftrainingsystembackend.http.MyHttpResponseObj;
import com.huajuan.stafftrainingsystembackend.request.instructor.RegisterScoreRequest;
import com.huajuan.stafftrainingsystembackend.service.CourseService;
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
public class InstructorController {

    @Autowired
    private CourseService courseService;

    /**
     * 教员调用这个接口，找到他所教授的每一门课程，并包含所有学生在这门课程下的成绩
     *
     * @return 他所教授的每一门课程，并包含所有学生在这门课程下的成绩
     */
    @GetMapping("/instructor/my_course_students")
    @PreAuthorize("hasAuthority('instructor')")
    public ResponseEntity<List<TaughtCourseDTO>> myCourseStudents(HttpServletRequest httpReq) {
        String instructorID = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));
        return ResponseEntity.ok(courseService.allTaughtCourseDTO(
                instructorID
        ));
    }


    @PostMapping("/instructor/register_score")
    @PreAuthorize("hasAuthority('instructor')")
    public ResponseEntity<MyHttpResponseObj> registerScore(
            HttpServletRequest httpReq,
            @Valid @RequestBody RegisterScoreRequest registerScoreRequest,
            BindingResult result) {

        //从http头中获得操作者
        String operator = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        if (result.hasFieldErrors()) {
            throw new RuntimeException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //登记成绩
        courseService.registerScore(registerScoreRequest, operator);

        return ResponseEntity.ok(new MyHttpResponseObj(HttpServletResponse.SC_OK, "操作成功"));
    }
}
