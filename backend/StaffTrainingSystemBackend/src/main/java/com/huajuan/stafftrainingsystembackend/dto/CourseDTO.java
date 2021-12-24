package com.huajuan.stafftrainingsystembackend.dto;


import com.huajuan.stafftrainingsystembackend.entity.DeptCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 用来在管理员查询所有的课程，显示的课程对象
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private String courseID;
    private String courseName;
    private String courseContent;
    private String courseType;
    private String instructorID;
    private String instructorName;
    private List<DeptCourseDTO> dept;

    public CourseDTO(String courseID, String courseName, String courseContent, String courseType, String instructorID, String instructorName) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseContent = courseContent;
        this.courseType = courseType;
        this.instructorID = instructorID;
        this.instructorName = instructorName;
    }
}
