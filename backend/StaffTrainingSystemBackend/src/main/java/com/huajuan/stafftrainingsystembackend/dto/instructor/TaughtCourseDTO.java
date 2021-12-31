package com.huajuan.stafftrainingsystembackend.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 用来显示给教员的，他教授的课程的DTO
 * 含有课程信息和课程编号，和他在这门课下教授的学生的成绩
 * 在/instructor/my_students中显示学生的成绩时使用
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaughtCourseDTO {
    private String courseName;
    private String courseID;
    private List<TaughtScoreDTO> studentScores;//他在这门课上教的学生的成绩

    public TaughtCourseDTO(String courseName, String courseID) {
        this.courseName = courseName;
        this.courseID = courseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaughtCourseDTO that = (TaughtCourseDTO) o;
        return courseName.equals(that.courseName) && courseID.equals(that.courseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, courseID);
    }
}
