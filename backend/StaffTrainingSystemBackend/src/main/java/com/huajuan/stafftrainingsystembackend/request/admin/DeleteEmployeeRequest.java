package com.huajuan.stafftrainingsystembackend.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteEmployeeRequest {

    @NotNull(message = "员工编号不能为空")
    private String employeeID;
}
