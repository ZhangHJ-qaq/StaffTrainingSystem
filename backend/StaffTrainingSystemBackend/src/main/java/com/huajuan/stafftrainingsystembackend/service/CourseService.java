package com.huajuan.stafftrainingsystembackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtCourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtScoreDTO;
import com.huajuan.stafftrainingsystembackend.entity.*;
import com.huajuan.stafftrainingsystembackend.repository.*;
import com.huajuan.stafftrainingsystembackend.request.admin.ModifyCourseRequest;
import com.huajuan.stafftrainingsystembackend.request.instructor.RegisterScoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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


    /**
     * 获得所有课程的信息
     *
     * @return 所有课程的信息，以列表的方式出现
     */
    public List<CourseDTO> allCourseInfo() {

        //获得courseDTO的列表，但是还没有注入部门信息
        List<CourseDTO> courseDTOList = courseRepository.findAllCourseDTO();

        //给每个courseDTO注入部门的信息
        courseDTOList.forEach(courseDTO -> {
            courseDTO.setDept(
                    deptCourseRepository.findDeptCourseDTOWithCourseID(courseDTO.getCourseID())
            );
        });

        return courseDTOList;
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


}
