package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {

    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.CourseDTO(c.courseID,c.courseName,c.courseContent,c.courseType,t.instructorID,e.name)" +
            " from Course c left join Teach t on t.courseID=c.courseID left join Employee e on t.instructorID=e.employeeID")
    public List<CourseDTO> findAllCourseDTO();


    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.CourseDTO(c.courseID,c.courseName,c.courseContent,c.courseType,t.instructorID,e.name) from Course c left join DeptCourse dc on c.courseID=dc.courseID left join Teach t on t.courseID=c.courseID left join Employee e on e.employeeID=t.instructorID where dc.deptID=:deptID")
    public List<CourseDTO> findAllCourseDTOWithDeptID(Integer deptID);

    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.CourseDTO(c.courseID,c.courseName,c.courseContent,c.courseType,t.instructorID,e.name) from Course c left join DeptCourse dc on c.courseID=dc.courseID left join Teach t on t.courseID=c.courseID left join Employee e on e.employeeID=t.instructorID where dc.deptID=:deptID and dc.compulsory=true ")
    public List<CourseDTO> findAllCompulsoryCourseDTOWithDeptID(Integer deptID);
}
