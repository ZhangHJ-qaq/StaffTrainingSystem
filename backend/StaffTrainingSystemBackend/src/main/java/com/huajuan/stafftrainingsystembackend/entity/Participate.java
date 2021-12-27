package com.huajuan.stafftrainingsystembackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "participate")
public class Participate {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "participate_id")
    private Long participateID;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "finished")
    private boolean finished;

    @Column(name = "score")
    private Integer score;

    @Column(name = "instructor_id")
    private String instructorID;

    @Column(name = "student_id")
    private String studentID;

    @Column(name = "course_id")
    private String courseID;
}
