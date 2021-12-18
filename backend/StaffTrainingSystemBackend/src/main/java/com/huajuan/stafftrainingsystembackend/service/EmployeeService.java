package com.huajuan.stafftrainingsystembackend.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.entity.*;
import com.huajuan.stafftrainingsystembackend.repository.*;
import com.huajuan.stafftrainingsystembackend.request.ModifyEmployeeRequest;
import org.apache.log4j.spi.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.executable.ValidateOnExecution;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DeptManagerRepository deptManagerRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private LogRepository logRepository;

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


    /**
     * 根据请求中的内容，添加或修改一个员工的信息
     *
     * @param req 员工的数据信息
     */
    @Transactional
    public void modifyEmployee(ModifyEmployeeRequest req, String operator) {
        String role = req.getRole();

        Employee employee = new Employee(
                req.getEmployeeID(),
                req.getName(),
                req.getPassword(),
                req.getGender(),
                req.getArrivalTime(),
                req.getEmail(),
                req.getPhoneNumber(),
                req.getRole(),
                req.getDeptID()
        );

        employeeRepository.save(employee);

        //如果要切换角色，需要清除原来的角色信息
        //如果本来就没有，会抛出异常，这里我们简单地将异常忽略掉即可
        try {
            if (studentRepository.existsById(req.getEmployeeID())) {
                studentRepository.deleteById(req.getEmployeeID());
            }
            if (deptManagerRepository.existsById(req.getEmployeeID())) {
                deptManagerRepository.deleteById(req.getEmployeeID());
            }
            if (instructorRepository.existsById(req.getEmployeeID())) {
                instructorRepository.deleteById(req.getEmployeeID());
            }
        } catch (Exception ignored) {
        }

        switch (role) {
            case "student":
                studentRepository.save(new Student(req.getEmployeeID()));
                break;
            case "instructor":
                instructorRepository.save(new Instructor(req.getEmployeeID()));
                break;
            case "department_manager":
                deptManagerRepository.save(new DeptManager(req.getEmployeeID()));
                break;
            default:
                throw new RuntimeException("角色错误！");
        }


        //写日志
        try {
            logRepository.save(new Log(
                    null,
                    String.format("修改员工为%s", new ObjectMapper().writeValueAsString(employee)),
                    new Date(), operator
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
