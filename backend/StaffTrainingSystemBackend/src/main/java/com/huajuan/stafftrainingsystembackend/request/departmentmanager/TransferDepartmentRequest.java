package com.huajuan.stafftrainingsystembackend.request.departmentmanager;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferDepartmentRequest {
    private String employeeID;
    private String name;

    @NotNull(message = "新部门的编号不能为空")
    private Integer newDeptID;
}
