package com.huajuan.stafftrainingsystembackend.dto.departmentmanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferQualificationDTO {
    private String employeeID;
    private String name;
    private Boolean canTransfer;
    private String detail;
}
