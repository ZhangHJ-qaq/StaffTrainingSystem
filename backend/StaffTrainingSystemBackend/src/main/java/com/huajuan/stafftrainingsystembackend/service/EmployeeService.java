package com.huajuan.stafftrainingsystembackend.service;

import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.entity.Employee;
import com.huajuan.stafftrainingsystembackend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        //在数据库中查找Employee
        Employee employee = employeeRepository.findEmployeeByEmployeeID(userID);

        //如果找不到也不能返回null，否则会违反接口的约定
        if (employee == null) {
            employee = new Employee();
        }

        return employee;
    }


    /**
     * 获得所有员工的信息
     *
     * @return 所有员工的信息，放在List<EmployeeDTO>里
     */
    public List<EmployeeDTO> allEmployeeInfo() {
        return employeeRepository.findAllEmployeeDTO();
    }
}
