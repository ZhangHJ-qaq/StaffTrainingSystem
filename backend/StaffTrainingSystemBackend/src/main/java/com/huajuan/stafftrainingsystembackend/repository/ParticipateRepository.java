package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.dto.ScoreDTO;
import com.huajuan.stafftrainingsystembackend.entity.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {

    @Query("select new com.huajuan.stafftrainingsystembackend.dto.ScoreDTO(p.participateID,p.courseID,c.courseName,p.startDate,p.finished,ins.name,p.score) from Participate p left join Course c on p.courseID=c.courseID left join Employee ins on ins.employeeID=p.instructorID where p.studentID=:studentID")
    public List<ScoreDTO> findAllScoreDTOWithStudentID(@Param("studentID") String studentID);
}
