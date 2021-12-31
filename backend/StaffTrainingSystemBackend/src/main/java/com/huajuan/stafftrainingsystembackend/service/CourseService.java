package com.huajuan.stafftrainingsystembackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.dto.ScoreDTO;
import com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtCourseDTO;
import com.huajuan.stafftrainingsystembackend.entity.*;
import com.huajuan.stafftrainingsystembackend.repository.*;
import com.huajuan.stafftrainingsystembackend.request.admin.DeleteCourseRequest;
import com.huajuan.stafftrainingsystembackend.request.admin.ModifyCourseRequest;
import com.huajuan.stafftrainingsystembackend.request.departmentmanager.AllocateCourseRequest;
import com.huajuan.stafftrainingsystembackend.request.instructor.RegisterScoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DeptCourseRepository deptCourseRepository;

    @Autowired
    private TeachRepository teachRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ParticipateRepository participateRepository;

    @Autowired
    private ManageRepository manageRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    @Lazy
    private EmployeeService employeeService;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 获得所有课程的信息
     *
     * @return 所有课程的信息，以列表的方式出现
     */
    public List<CourseDTO> allCourseInfo() {

        //获得courseDTO的列表，但是还没有注入部门信息
        List<CourseDTO> courseDTOList = courseRepository.findAllCourseDTO();

        //给每个courseDTO注入部门的信息
        courseDTOList.forEach(courseDTO -> courseDTO.setDept(
                deptCourseRepository.findDeptCourseDTOWithCourseID(courseDTO.getCourseID())
        ));

        return courseDTOList;
    }

    /**
     * 部门经理根据自己的deptManagerID，获得自己部门下所有课程的信息
     *
     * @param deptManagerID 部门经理ID
     * @return 自己部门下所有课程的信息
     */
    public List<CourseDTO> allCourseInfoInMyDepartment(String deptManagerID) {

        //找出管理的部门
        Manage manage = manageRepository.findByDeptManagerID(deptManagerID);
        if (manage == null) {
            throw new RuntimeException("未找到你所管理的部门，请联系系统管理员");
        }


        //获得部门下的所有课程
        List<CourseDTO> res = courseRepository.findAllCourseDTOWithDeptID(manage.getDeptID());

        //注入部门信息
        res.forEach(courseDTO -> courseDTO.setDept(deptCourseRepository.findDeptCourseDTOWithCourseID(courseDTO.getCourseID())));

        return res;
    }

    /**
     * 删除/添加课程
     *
     * @param req 删除/添加课程的请求
     */
    @Transactional
    public void modifyCourse(ModifyCourseRequest req, String operator) {

        //创建course对象并插入
        Course course = new Course(
                req.getCourseID(),
                req.getCourseType(),
                req.getCourseName(),
                req.getCourseContent()
        );

        courseRepository.save(course);

        Instructor instructor = instructorRepository.findById(req.getInstructorID()).orElse(null);
        if (instructor == null) {
            throw new RuntimeException(String.format("%s不是教员", req.getInstructorID()));
        }

        //更新课程教授的信息
        Teach teach = new Teach(req.getCourseID(), req.getInstructorID());
        teachRepository.save(teach);

        //更新和课程有关的部门信息

        //先删除所有和本课程有关的部门信息
        deptCourseRepository.deleteDeptCourseByCourseID(course.getCourseID());

        //更新本课程相关的部门信息
        req.getDept().forEach(deptCourseDTO -> {
            deptCourseRepository.save(new DeptCourse(
                    deptCourseDTO.getDeptID(),
                    course.getCourseID(),
                    deptCourseDTO.getCompulsory()));
        });

        try {
            //更新日志
            logRepository.save(new Log(
                    null,
                    String.format("修改课程%s", new ObjectMapper().writeValueAsString(course)),
                    new Date(),
                    operator
            ));
        } catch (JsonProcessingException ignored) {
        }

    }


    /**
     * 根据instructorID，找到这个instructor教的所有课程的DTO，包括所有学生在这门课下面的成绩
     *
     * @param instructorID 教员ID
     * @return 这个instructor教的所有课程的DTO，包括所有学生在这门课下面的成绩
     */
    public List<TaughtCourseDTO> allTaughtCourseDTO(String instructorID) {
        List<TaughtCourseDTO> taughtCourseDTOList = participateRepository.findAllTaughtCourseWithInstructorID(instructorID);
        taughtCourseDTOList.forEach(taughtCourseDTO -> {
            taughtCourseDTO.setStudentScores(participateRepository.findAllTaughtScoreWithCourseIDAndInstructorID(
                    taughtCourseDTO.getCourseID(), instructorID
            ));
        });
        return taughtCourseDTOList;
    }

    /**
     * 登记一个participateID的成绩
     *
     * @param registerScoreRequest 登记成绩请求
     * @param operator             操作者
     */
    @Transactional
    public void registerScore(RegisterScoreRequest registerScoreRequest, String operator) {
        Participate participate = participateRepository.findById(registerScoreRequest.getParticipateID()).orElse(null);
        if (participate == null) {
            throw new RuntimeException("课程记录不存在");
        }

        if (participate.isFinished()) {
            throw new RuntimeException("不能为已经结束的课程等级成绩");
        }

        //更新成绩
        participate.setScore(registerScoreRequest.getScore());
        participate.setFinished(true);

        participateRepository.save(participate);

        //写日志
        logRepository.save(new Log(
                null,
                String.format("登记participateID:%d的成绩为%d", registerScoreRequest.getParticipateID(), registerScoreRequest.getScore()),
                new Date(),
                operator
        ));

    }


    /**
     * 根据studentID和deptID，为这个学生分配
     *
     * @param studentID
     * @param deptID
     */
    @Transactional
    public void allocateCompulsoryCourses(String studentID, Integer deptID, String operator) {

        //要为这个学员分配的课程的编号。先查出这个部门所有的必修课程
        Set<String> allDeptCompulsoryCourseIDNeedToAllocate = new HashSet<>();

        //获得这个部门所有必修的课程
        List<CourseDTO> allDeptCompulsoryCourseDTO = courseRepository.findAllCompulsoryCourseDTOWithDeptID(deptID);
        allDeptCompulsoryCourseDTO.forEach(courseDTO -> {
            allDeptCompulsoryCourseIDNeedToAllocate.add(courseDTO.getCourseID());
        });

        //获得这个学员正在进行的，或者结束了且已经通过的课程
        List<ScoreDTO> pendingOrPassedScoreDTO = participateRepository.findAllPendingOrPassedScoreDTOWithStudentID(studentID);

        //把已经通过的课程，或者正在进行的课程从所有的必修课程里减掉，就是要给学生分配的课程
        pendingOrPassedScoreDTO.forEach(scoreDTO -> allDeptCompulsoryCourseIDNeedToAllocate.remove(scoreDTO.getCourseID()));

        //分配
        allDeptCompulsoryCourseIDNeedToAllocate.forEach(courseID -> {
            //添加学生在这门课程中的参与
            Participate participate = new Participate(
                    null,
                    new Date(),
                    false,
                    null,
                    teachRepository.getById(courseID).getInstructorID(),
                    studentID,
                    courseID
            );

            //记录参与信息
            participateRepository.save(participate);

            //记录日志
            logRepository.save(
                    new Log(
                            null,
                            String.format("为学员%s分配课程%s", studentID, courseID),
                            new Date(),
                            operator
                    )
            );

        });

    }


    /**
     * 分配课程
     *
     * @param allocateCourseRequest 分配课程的请求
     * @param deptManagerID         部门经理ID
     */
    @Transactional
    public void allocateCourse(AllocateCourseRequest allocateCourseRequest, String deptManagerID) {

        //先获得学生的ID
        String employeeID = allocateCourseRequest.getEmployeeID();
        String courseID = allocateCourseRequest.getCourseID();

        //如果学生id是空的话，就先根据名字找学生id
        if (employeeID == null) {
            String name = allocateCourseRequest.getName();
            employeeID = employeeService.findUniqueEmployeeIDByName(name);
        }

        if (!studentRepository.existsById(employeeID)) {//检查这个ID的主人是不是学生
            throw new RuntimeException("你输入的可能不是学员，必须输入一个学员的ID才能为其分配课程！");
        }

        if (!employeeService.deptManagerAndEmployeeInSameDepartment(deptManagerID, employeeID)) {
            throw new RuntimeException("你不能为一个不属于自己部门的员工分配课程");
        }

        //尝试插入Participate对象
        Participate participate = new Participate(
                null,
                new Date(),
                false,
                null,
                teachRepository.getById(courseID).getInstructorID(),
                employeeID,
                courseID
        );

        //保存
        participateRepository.save(participate);

        //写日志
        logRepository.save(
                new Log(
                        null,
                        String.format("为学员%s分配课程%s", employeeID, courseID),
                        new Date(),
                        deptManagerID
                )
        );

    }

    /**
     * 部门经理查询自己部门下的员工，如果转到一个新的部门，转完后需要额外修哪些课
     *
     * @param deptManagerID 部门经理编号
     * @param employeeID    员工编号
     * @param newDeptID     新部门编号
     * @return 转完以后需要额外修哪些课
     */
    public List<CourseDTO> getCoursesAfterTransfer(String deptManagerID, String employeeID, Integer newDeptID) {
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

        //找到所有新部门的必修课
        List<CourseDTO> compulsoryCoursesOfNewDepartment = courseRepository.findAllCompulsoryCourseDTOWithDeptID(newDeptID);

        //所有他已经通过的课程的课程ID
        Set<String> passedCourseID =
                participateRepository.findAllPassedScoreDTOWithStudentID(employeeID)
                        .stream().map(ScoreDTO::getCourseID).collect(Collectors.toSet());

        List<CourseDTO> coursesNeedToLearnAfterTransfer = new ArrayList<>();

        //遍历新部门的所有必修课，如果他还没有通过这门课，则这门课就是他转部门以后要学的课
        compulsoryCoursesOfNewDepartment.forEach(courseDTO -> {
            if (!passedCourseID.contains(courseDTO.getCourseID())) {
                courseDTO.setDept(deptCourseRepository.findDeptCourseDTOWithCourseID(courseDTO.getCourseID()));
                coursesNeedToLearnAfterTransfer.add(courseDTO);
            }
        });

        return coursesNeedToLearnAfterTransfer;

    }


    public void deleteCourse(DeleteCourseRequest deleteCourseRequest, String operator) {

        try {
            courseRepository.deleteById(deleteCourseRequest.getCourseID());

        } catch (Exception e) {
            throw new RuntimeException("无法删除此课程");
        }

        logRepository.save(new Log(
                null,
                String.format("删除课程%s", operator),
                new Date(),
                operator
        ));
    }

}
