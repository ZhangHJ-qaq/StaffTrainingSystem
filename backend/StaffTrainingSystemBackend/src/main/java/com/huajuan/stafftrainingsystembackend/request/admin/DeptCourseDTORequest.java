package com.huajuan.stafftrainingsystembackend.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeptCourseDTORequest {
    @NotNull(message = "部门编号不能为空给")
    private Integer deptID;
    private String deptName;
    @NotNull(message = "是否必修不能为空")
    private Boolean compulsory;
}

