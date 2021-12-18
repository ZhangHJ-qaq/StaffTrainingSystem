package com.huajuan.stafftrainingsystembackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeptCourseDTO {

    private Integer deptID;
    private String deptName;
    private Boolean compulsory;
}
