package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.dto.CourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.ScoreDTO;
import com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtCourseDTO;
import com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtScoreDTO;
import com.huajuan.stafftrainingsystembackend.entity.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {

    @Query("select new com.huajuan.stafftrainingsystembackend.dto.ScoreDTO(p.participateID,p.courseID,c.courseName,p.startDate,p.finished,ins.name,p.score) from Participate p left join Course c on p.courseID=c.courseID left join Employee ins on ins.employeeID=p.instructorID where p.studentID=:studentID")
    public List<ScoreDTO> findAllScoreDTOWithStudentID(@Param("studentID") String studentID);

    /**
     * 由instructor使用，根据instructorID，在学生的participate记录里，找到这个instructor所有上过的课程
     *
     * @param instructorID 教员ID
     * @return 这个教员上过的所有课程
     */
    @Query("select distinct new com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtCourseDTO(c.courseName,p.courseID) from Participate p left join Course c on p.courseID=c.courseID where p.instructorID=:instructorID")
    public List<TaughtCourseDTO> findAllTaughtCourseWithInstructorID(@Param("instructorID") String instructorID);

    /**
     * 由instructor使用，根据instructorID和courseID，在学生的participate记录里，找到这个instructor上的这门课程里，所有学生的成绩记录
     *
     * @param courseID     课程编号
     * @param instructorID 教员编号
     * @return 找到这个instructor上的这门课程里，所有学生的成绩记录
     */

    @Query("select new com.huajuan.stafftrainingsystembackend.dto.instructor.TaughtScoreDTO(p.participateID,p.studentID,e.name,p.startDate,p.finished,p.score) from Participate p left join Course c on p.courseID=c.courseID left join Employee e on p.studentID=e.employeeID where p.courseID=:courseID and p.instructorID=:instructorID")
    public List<TaughtScoreDTO> findAllTaughtScoreWithCourseIDAndInstructorID(
            @Param("courseID") String courseID,
            @Param("instructorID") String instructorID);


    /**
     * 根据学生的编号，找到他所有正在进行的课程，或者已经通过的课程的成绩信息
     *
     * @param studentID 学生编号
     * @return 他所有正在进行的课程，或者已经通过的课程的成绩信息
     */
    @Query("select new com.huajuan.stafftrainingsystembackend.dto.ScoreDTO(p.participateID,p.courseID,c.courseName,p.startDate,p.finished,ins.name,p.score) from Participate p left join Course c on p.courseID=c.courseID left join Employee ins on ins.employeeID=p.instructorID where p.studentID=:studentID and (p.finished=false or p.score>=60)")
    public List<ScoreDTO> findAllPendingOrPassedScoreDTOWithStudentID(@Param("studentID") String studentID);

    /**
     * 根据学生的编号，找到他所有已经通过的课程的成绩信息
     *
     * @param studentID 学生编号
     * @return 他所有正在进行的课程，或者已经通过的课程的成绩信息
     */
    @Query("select new com.huajuan.stafftrainingsystembackend.dto.ScoreDTO(p.participateID,p.courseID,c.courseName,p.startDate,p.finished,ins.name,p.score) from Participate p left join Course c on p.courseID=c.courseID left join Employee ins on ins.employeeID=p.instructorID where p.studentID=:studentID and p.score>=60")
    public List<ScoreDTO> findAllPassedScoreDTOWithStudentID(@Param("studentID") String studentID);


    /**
     * 根据学生的编号，找到学生所有没有完成的，或是挂掉的participate记录
     *
     * @param studentID 学生编号
     * @return 学生所有没有完成的，或是挂掉的participate记录
     */
    @Query("select new com.huajuan.stafftrainingsystembackend.dto.ScoreDTO(p.participateID,p.courseID,c.courseName,p.startDate,p.finished,ins.name,p.score) from Participate p left join Course c on p.courseID=c.courseID left join Employee ins on ins.employeeID=p.instructorID where p.studentID=:studentID and (p.finished=false or p.score<60)")
    public List<ScoreDTO> findAllPendingOrFailedScoreDTOWithStudentID(@Param("studentID") String studentID);
}
