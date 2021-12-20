package com.huajuan.stafftrainingsystembackend.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyEmployeeRequest {

    @NotNull(message = "员工编号不能为空")
    private String employeeID;

    @NotNull(message = "姓名不能为空")
    private String name;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "性别不能为空")
    private String gender;

    @NotNull(message = "入职日期不能为空")
    private Date arrivalTime;

    @NotNull(message = "手机号不能为空")
    private String phoneNumber;

    @Email(message = "邮箱不符合格式")
    private String email;

    @NotNull(message = "角色不能为空")
    private String role;

    @NotNull(message = "部门不能为空")
    private Integer deptID;

}
