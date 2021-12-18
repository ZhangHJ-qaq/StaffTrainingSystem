package com.huajuan.stafftrainingsystembackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.entity.Course;
import com.huajuan.stafftrainingsystembackend.entity.DeptCourse;
import com.huajuan.stafftrainingsystembackend.entity.Log;
import com.huajuan.stafftrainingsystembackend.entity.Teach;
import com.huajuan.stafftrainingsystembackend.repository.CourseRepository;
import com.huajuan.stafftrainingsystembackend.repository.DeptCourseRepository;
import com.huajuan.stafftrainingsystembackend.repository.LogRepository;
import com.huajuan.stafftrainingsystembackend.repository.TeachRepository;
import com.huajuan.stafftrainingsystembackend.request.ModifyCourseRequest;
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
    private LogRepository logRepository;


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

}
