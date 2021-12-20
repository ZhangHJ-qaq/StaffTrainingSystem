package com.huajuan.stafftrainingsystembackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.dto.ScoreDTO;
import com.huajuan.stafftrainingsystembackend.entity.*;
import com.huajuan.stafftrainingsystembackend.repository.*;
import com.huajuan.stafftrainingsystembackend.request.admin.ModifyEmployeeRequest;
import com.huajuan.stafftrainingsystembackend.request.student.StudentModifySelfInfoRequest;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private ParticipateRepository participateRepository;

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

        Employee originalEmployee = employeeRepository.findEmployeeByEmployeeID(req.getEmployeeID());
        if (originalEmployee != null
                && !Objects.equals(originalEmployee.getRole(), role)) {
            throw new RuntimeException("不能修改用户的角色");
        }

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

        //如果是新增用户
        if (originalEmployee == null) {
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
                case "admin":
                    break;
                default:
                    throw new RuntimeException("角色错误！");
            }
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

    /**
     * 根据员工的名字，查询员工的信息和成绩
     *
     * @param name 名字
     * @return 员工的个人信息和成绩
     */
    public EmployeeDTO findEmployeeDTOWithScoreWithName(String name) {
        EmployeeDTO employeeDTO = employeeRepository.findEmployeeDTOWithoutScoreWithName(name);

        if (employeeDTO == null) {
            throw new RuntimeException(String.format("找不到名为%s的员工", name));
        }
        //注入成绩
        employeeDTO.setScores(participateRepository.findAllScoreDTOWithStudentID(employeeDTO.getEmployeeID()));
        return employeeDTO;
    }

    /**
     * 根据员工的编号，查询员工的信息和成绩
     *
     * @param employeeID 名字
     * @return 员工的个人信息和成绩
     */
    public EmployeeDTO findEmployeeDTOWithScoreWithEmployeeID(String employeeID) {
        EmployeeDTO employeeDTO = employeeRepository.findEmployeeDTOWithoutScoreWithEmployeeID(employeeID);

        if (employeeDTO == null) {
            throw new RuntimeException(String.format("找不到编号为%s的员工", employeeID));
        }
        //注入成绩
        employeeDTO.setScores(participateRepository.findAllScoreDTOWithStudentID(employeeDTO.getEmployeeID()));
        return employeeDTO;
    }

    /**
     * 查询员工的个人信息，不带有成绩
     *
     * @param employeeID 员工编号
     * @return EmployeeDTO
     */
    public EmployeeDTO findEmployeeByEmployeeIDWithoutScore(String employeeID) {
        return employeeRepository.findEmployeeDTOWithoutScoreWithEmployeeID(employeeID);
    }


    public void modifyStudent(String employeeID, StudentModifySelfInfoRequest req) {
        Employee student = employeeRepository.findById(employeeID).orElse(null);
        if (student == null) {
            throw new RuntimeException("找不到学员信息");
        }
        student.setName(req.getName());
        student.setGender(req.getGender());
        student.setEmail(req.getEmail());
        student.setPhoneNumber(req.getPhoneNumber());
        employeeRepository.save(student);
    }

    /**
     * 根据员工ID，获得员工正在进行的课程信息
     *
     * @param employeeID 员工ID
     * @return 员工正在进行的课程信息
     */
    public List<ScoreDTO> findCurrentCourses(String employeeID) {

        //直接获取全部的在应用层过滤一下吧
        List<ScoreDTO> allScoresDTO = participateRepository.findAllScoreDTOWithStudentID(employeeID);

        return allScoresDTO.stream().filter(scoreDTO -> {
            return !scoreDTO.getFinished();
        }).collect(Collectors.toList());
    }

    /**
     * 根据员工ID，获取历史课程
     * @param employeeID 员工ID
     * @return 历史课程的信息和成绩
     */
    public List<ScoreDTO> findHistoryCourses(String employeeID) {
        //直接获取全部的在应用层过滤一下吧
        List<ScoreDTO> allScoresDTO = participateRepository.findAllScoreDTOWithStudentID(employeeID);

        return allScoresDTO.stream().filter(ScoreDTO::getFinished).collect(Collectors.toList());
    }
}
