package com.huajuan.stafftrainingsystembackend.dto;

import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 用来在员工查询个人信息时，或者管理员查询某个员工的信息时，显示的员工对象
 */
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
    private List<ScoreDTO> scores;

    public EmployeeDTO(String employeeID, String name, String password, String gender, Date arrivalTime, String email, String phoneNumber, String role, String dept_name, Integer deptID) {
        this.employeeID = employeeID;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.arrivalTime = arrivalTime;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.dept_name = dept_name;
        this.deptID = deptID;
    }
}
