package com.huajuan.stafftrainingsystembackend.request.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCourseRequest {

    @NotNull(message = "课程编号不能为空")
    private String courseID;

    @NotNull(message = "课程名字不能为空")
    private String courseName;

    @NotNull(message = "课程内容不能为空")
    private String courseContent;

    @NotNull(message = "课程类型不能为空")
    private String courseType;

    @NotNull(message = "教员编号不能为空")
    private String instructorID;

    @NotNull(message = "教员名字不能为空")
    private String instructorName;

    @NotNull(message = "部门信息不能为空")
    @NotEmpty
    private List<@Valid @NotNull DeptCourseDTORequest> dept;


}

