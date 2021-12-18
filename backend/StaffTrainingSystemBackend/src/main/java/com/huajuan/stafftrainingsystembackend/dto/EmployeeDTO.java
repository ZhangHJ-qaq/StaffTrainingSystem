package com.huajuan.stafftrainingsystembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private String employeeID;
    private String name;
    private String password;
    private String gender;
    private Date arrivalTime;
    private String email;
    private String phoneNumber;
    private String role;
    private String dept_name;
    private Integer deptID;
}
