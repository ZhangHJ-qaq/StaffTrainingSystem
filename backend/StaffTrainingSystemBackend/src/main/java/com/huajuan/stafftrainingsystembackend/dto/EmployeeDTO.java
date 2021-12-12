package com.huajuan.stafftrainingsystembackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
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

    public EmployeeDTO(String employeeID, String name, String password, String gender, Date arrivalTime, String email, String phoneNumber, String role, String dept_name) {
        this.employeeID = employeeID;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.arrivalTime = arrivalTime;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.dept_name = dept_name;
    }
}
