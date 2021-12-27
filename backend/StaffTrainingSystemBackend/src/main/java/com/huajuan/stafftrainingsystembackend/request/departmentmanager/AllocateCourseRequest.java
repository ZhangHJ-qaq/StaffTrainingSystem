package com.huajuan.stafftrainingsystembackend.request.departmentmanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 部门经理给员工分配课程的请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AllocateCourseRequest {

    private String employeeID;
    private String name;

    @NotNull(message = "课程编号不能为空")
    private String courseID;

}
