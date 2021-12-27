package com.huajuan.stafftrainingsystembackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.dto.ScoreDTO;
import com.huajuan.stafftrainingsystembackend.dto.departmentmanager.TransferQualificationDTO;
import com.huajuan.stafftrainingsystembackend.entity.*;
import com.huajuan.stafftrainingsystembackend.repository.*;
import com.huajuan.stafftrainingsystembackend.request.admin.ModifyEmployeeRequest;
import com.huajuan.stafftrainingsystembackend.request.departmentmanager.TransferDepartmentRequest;
import com.huajuan.stafftrainingsystembackend.request.student.StudentModifySelfInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
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

    @Autowired
    private ManageRepository manageRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentRepository departmentRepository;

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
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllEmployeeDTO();
        //注入成绩
        employeeDTOList.forEach(employeeDTO -> {
            employeeDTO.setScores(
                    participateRepository.findAllScoreDTOWithStudentID(employeeDTO.getEmployeeID())
            );
        });
        return employeeDTOList;
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
                    //如果是学员，就为他分配部门的课程信息
                    courseService.allocateCompulsoryCourses(
                            req.getEmployeeID(),
                            req.getDeptID(),
                            operator
                    );

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

        if ("department_manager".equals(role)) {

            //限定一个部门的部门经理最多只能有一个
            List<Manage> manageList = manageRepository.findAllByDeptID(employee.getDeptID());
            if (!manageList.isEmpty()) { //如果部门已经有部门经理了
                throw new RuntimeException(String.format("该部门已经存在经理%s", manageList.get(0).getDeptManagerID()));
            }

            //在manage表中更改部门经理有关的信息
            Manage manage = new Manage(employee.getDeptID(), employee.getEmployeeID());
            manageRepository.save(manage);
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
        String employeeID = findUniqueEmployeeIDByName(name);

        EmployeeDTO employeeDTO = employeeRepository.findEmployeeDTOWithoutScoreWithEmployeeID(employeeID);

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

        return allScoresDTO.stream().filter(scoreDTO -> !scoreDTO.getFinished()).collect(Collectors.toList());
    }

    /**
     * 根据员工ID，获取历史课程
     *
     * @param employeeID 员工ID
     * @return 历史课程的信息和成绩
     */
    public List<ScoreDTO> findHistoryCourses(String employeeID) {
        //直接获取全部的在应用层过滤一下吧
        List<ScoreDTO> allScoresDTO = participateRepository.findAllScoreDTOWithStudentID(employeeID);

        return allScoresDTO.stream().filter(ScoreDTO::getFinished).collect(Collectors.toList());
    }


    /**
     * 根据一个部门经理的deptManagerID，找到他部门底下所有的员工的信息
     *
     * @param deptManagerID 部门经理ID
     * @return 部门经理所管理的部门下面所有员工的信息
     */
    public List<EmployeeDTO> allEmployeeInfoInMyDepartment(String deptManagerID) {

        //先根据deptManagerID找到所管理的部门
        Manage manage = manageRepository.findByDeptManagerID(deptManagerID);
        if (manage == null) {
            throw new RuntimeException("未找到你所管理的部门，请联系系统管理员");
        }

        List<EmployeeDTO> res = employeeRepository.findAllEmployeeDTOWithDeptID(manage.getDeptID());

        //注入成绩
        res.forEach(employeeDTO -> employeeDTO.setScores(participateRepository.findAllScoreDTOWithStudentID(employeeDTO.getEmployeeID())));

        return res;
    }


    /**
     * 根据姓名，查找出员工ID。如果不不存在这个姓名的员工，或是这个姓名的员工数量不止一个，就会抛出异常
     * 如果函数能返回，说明这个姓名只对应了一个员工ID
     *
     * @param name 姓名
     * @return 员工ID
     */
    public String findUniqueEmployeeIDByName(String name) {
        List<Employee> employeeList = employeeRepository.findEmployeeByName(name);
        if (employeeList.size() == 0) {
            //如果找不到员工
            throw new RuntimeException(String.format("找不到名为%s的员工", name));
        }
        if (employeeList.size() >= 2) {
            //如果员工数目多于2，就抛出异常，让其输入具体的员工编号查询
            List<String> employeeIDList = new ArrayList<>();
            employeeList.forEach(employee -> employeeIDList.add(employee.getEmployeeID()));
            throw new RuntimeException(String.format("名为%s的员工有%s，请使用员工编号进行查找", name, String.join(",", employeeIDList)));
        }
        //正常情况：一个名字只有一个员工
        return employeeList.get(0).getEmployeeID();
    }


    /**
     * 根据部门经理的id和员工的id，检查他们是否属于同一部门
     *
     * @param deptManagerID 部门经理id
     * @param employeeID    员工id
     * @return 他们是否属于同一部门
     */
    public Boolean deptManagerAndEmployeeInSameDepartment(String deptManagerID, String employeeID) {
        Manage manage = manageRepository.findByDeptManagerID(deptManagerID);
        Employee employee = employeeRepository.findEmployeeByEmployeeID(employeeID);
        if (manage == null) {
            throw new RuntimeException("未找到你所管理的部门，请联系系统管理员");
        }
        if (employee == null) {
            throw new RuntimeException("找不到员工");
        }
        return manage.getDeptID() == employee.getDeptID();
    }

    /**
     * 部门经理查询自己部门下，所有员工是否符合转部门的情况
     *
     * @param deptManagerID 部门经理ID
     * @return 自己部门下，所有员工是否符合转部门的情况
     */
    public List<TransferQualificationDTO> getTransferQualificationOfAllEmployeeInMyDepartment(String deptManagerID) {

        Manage manage = manageRepository.findByDeptManagerID(deptManagerID);
        if (manage == null) {
            throw new RuntimeException("未找到你所管理的部门，请联系系统管理员");
        }

        //先找到部门下的所有员工
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllEmployeeDTOWithDeptID(manage.getDeptID());

        //过滤掉非学员
        employeeDTOList = employeeDTOList.stream().filter(employeeDTO -> "student".equals(employeeDTO.getRole())).collect(Collectors.toList());

        List<TransferQualificationDTO> transferQualificationDTOS = new ArrayList<>();

        //逐个遍历，计算出每个员工的transferQualification
        employeeDTOList.forEach(employeeDTO -> {
            transferQualificationDTOS.add(getTransferQualificationDTO(employeeDTO));
        });

        return transferQualificationDTOS;

    }


    /**
     * 部门经理查询自己部门下的一个员工是否符合转部门的信息
     *
     * @param deptManagerID 部门经理ID
     * @return transferQualification对象
     */
    public TransferQualificationDTO getTransferQualificationOfOneEmployeeInMyDepartment(String deptManagerID, String employeeID) {
        Manage manage = manageRepository.findByDeptManagerID(deptManagerID);
        if (manage == null) {
            throw new RuntimeException("未找到你所管理的部门，请联系系统管理员");
        }

        EmployeeDTO employeeDTO = employeeRepository.findEmployeeDTOWithoutScoreWithEmployeeID(employeeID);

        if (employeeDTO == null) {
            throw new RuntimeException("找不到员工");
        }

        if (!"student".equals(employeeDTO.getRole())) {
            throw new RuntimeException("只有学员才可以转部门");
        }

        if (manage.getDeptID() != employeeDTO.getDeptID()) {
            throw new RuntimeException("你不能查询一个不属于自己部门的员工的信息");
        }

        return getTransferQualificationDTO(employeeDTO);
    }


    private TransferQualificationDTO getTransferQualificationDTO(EmployeeDTO employeeDTO) {
        List<ScoreDTO> pendingOrFailedScoreDTOList = participateRepository.findAllPendingOrFailedScoreDTOWithStudentID(employeeDTO.getEmployeeID());

        //如果没有未完成或不通过的课程
        if (pendingOrFailedScoreDTOList.size() == 0) {
            return new TransferQualificationDTO(
                    employeeDTO.getEmployeeID(),
                    employeeDTO.getName(),
                    true,
                    "可以转部门"
            );
        } else {
            //反之如果有课程未完成或者未通过，显示理由
            //使用一个list记录学员哪些课程没有通过，哪些课程没有完成的信息
            List<String> li = new ArrayList<>();
            pendingOrFailedScoreDTOList.forEach(scoreDTO -> {
                if (!scoreDTO.getFinished()) {
                    li.add(
                            scoreDTO.getCourseID() + " " + scoreDTO.getCourseName() + "未完成"
                    );
                } else if (scoreDTO.getScore() != null && scoreDTO.getScore() < 60) {
                    li.add(
                            scoreDTO.getCourseID() + " " + scoreDTO.getCourseName() + "未通过"
                    );
                }
            });

            String details = String.join(",", li);

            return new TransferQualificationDTO(
                    employeeDTO.getEmployeeID(),
                    employeeDTO.getName(),
                    false,
                    details
            );
        }

    }


    /**
     * 部门经理给自己部门下的员工转部门
     *
     * @param deptManagerID             部门经理的编号
     * @param transferDepartmentRequest 转部门的请求
     */
    @Transactional
    public void transferDepartment(String deptManagerID, TransferDepartmentRequest transferDepartmentRequest) {
        Manage manage = manageRepository.findByDeptManagerID(deptManagerID);
        if (manage == null) {
            throw new RuntimeException("未找到你所管理的部门，请联系系统管理员");
        }

        String employeeID = transferDepartmentRequest.getEmployeeID();
        String name = transferDepartmentRequest.getName();
        Integer newDeptID = transferDepartmentRequest.getNewDeptID();

        if (employeeID == null) {//如果没有传来员工ID，就根据name查找员工ID
            employeeID = findUniqueEmployeeIDByName(name);
        }

        Employee employee = employeeRepository.findById(employeeID).orElse(null);

        if (employee == null) {
            throw new RuntimeException("找不到员工");
        }
        Integer oldDept = employee.getDeptID();

        if (!"student".equals(employee.getRole())) {
            throw new RuntimeException("只有学员才可以转部门");
        }

        if (manage.getDeptID() != employee.getDeptID()) {
            throw new RuntimeException("你不能给不是自己部门的员工转部门");
        }

        //检查是否可以转部门
        TransferQualificationDTO transferQualificationDTO = getTransferQualificationOfOneEmployeeInMyDepartment(deptManagerID, employeeID);
        if (!transferQualificationDTO.getCanTransfer()) {
            throw new RuntimeException("转部门失败。因为" + transferQualificationDTO.getDetail());
        }

        //检查部门是否存在
        if (!departmentRepository.existsById(newDeptID)) {
            throw new RuntimeException("要转入的部门不存在！");
        }

        //更新employee条目中的部门信息
        employee.setDeptID(newDeptID);
        employeeRepository.save(employee);

        //分配新部门的必修课
        courseService.allocateCompulsoryCourses(employee.getEmployeeID(), newDeptID, deptManagerID);

        //记录转部门的日志
        logRepository.save(new Log(
                null,
                String.format("将员工%s从部门%d转到部门%d", employeeID, oldDept, newDeptID),
                new Date(),
                deptManagerID
        ));


    }


}
