package com.huajuan.stafftrainingsystembackend.controller;

import com.huajuan.stafftrainingsystembackend.contants.SecurityConstants;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.http.MyHttpResponseObj;
import com.huajuan.stafftrainingsystembackend.request.ModifyEmployeeRequest;
import com.huajuan.stafftrainingsystembackend.service.EmployeeService;
import com.huajuan.stafftrainingsystembackend.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class AdminController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 获得所有员工的信息
     *
     * @return
     */
    @GetMapping("/admin/all_employee_info")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Map<String, Object>> allEmployeeInfo() {
        List<EmployeeDTO> employeeDTOList = employeeService.allEmployeeInfo();
        Map<String, Object> res = new HashMap<>();
        res.put("all_employee_info", employeeDTOList);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/admin/modify_employee")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<MyHttpResponseObj> modifyEmployee(
            HttpServletRequest httpReq,
            @Valid @RequestBody ModifyEmployeeRequest modifyEmployeeRequest,
            BindingResult result) {

        //从http头中获得操作者
        String operator = JwtTokenUtils.getUsernameByAuthorization(httpReq.getHeader(SecurityConstants.TOKEN_HEADER));

        if (result.hasFieldErrors()) {
            throw new RuntimeException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        employeeService.modifyEmployee(modifyEmployeeRequest, operator);

        return ResponseEntity.ok(new MyHttpResponseObj(200, "操作成功"));
    }


}
