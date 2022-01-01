package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.dto.DeptCourseDTO;
import com.huajuan.stafftrainingsystembackend.entity.DeptCourse;
import com.huajuan.stafftrainingsystembackend.id.DeptCourseID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;


public interface DeptCourseRepository extends JpaRepository<DeptCourse, DeptCourseID> {


    @Query("select new com.huajuan.stafftrainingsystembackend.dto.DeptCourseDTO(dept.deptID,dept.deptName,dc.compulsory) from DeptCourse dc left join Department dept on dept.deptID=dc.deptID where dc.courseID=:courseID")
    public List<DeptCourseDTO> findDeptCourseDTOWithCourseID(@Param("courseID") String courseID);

    public void deleteDeptCourseByCourseID(String courseID);


    @Query("select new com.huajuan.stafftrainingsystembackend.dto.DeptCourseDTO(dept.deptID,dept.deptName,dc.compulsory) from DeptCourse dc left join Department dept on dept.deptID=dc.deptID where dc.courseID=:courseID and dc.deptID=:deptID")
    public DeptCourseDTO findDeptCourseDTOWithDeptIDAndCourseID(@Param("deptID") Integer deptID, @Param("courseID") String courseID);
}
