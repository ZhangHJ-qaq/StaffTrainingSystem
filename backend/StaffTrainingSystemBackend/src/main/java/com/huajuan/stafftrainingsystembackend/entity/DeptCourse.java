package com.huajuan.stafftrainingsystembackend.entity;


import com.huajuan.stafftrainingsystembackend.id.DeptCourseID;
import jdk.jfr.Enabled;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "dept_course")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(DeptCourseID.class)
public class DeptCourse implements Serializable {
    @Id
    @Column(name = "dept_id")
    private Integer deptID;

    @Id
    @Column(name = "course_id")
    private String courseID;

    @Column(name = "compulsory")
    private boolean compulsory;

}
