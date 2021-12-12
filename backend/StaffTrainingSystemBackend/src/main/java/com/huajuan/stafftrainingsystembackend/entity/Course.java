package com.huajuan.stafftrainingsystembackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
public class Course {

    /**
     * 课程号
     */
    @Id
    @Column(name = "course_id")
    private String courseID;

    /**
     * 课程类型
     */
    @Column(name = "course_type")
    private String courseType;

    /**
     * 课程名字
     */
    @Column(name = "course_name")
    private String courseName;

    /**
     * 课程内容的描述
     */
    @Column(name = "course_Content")
    private String courseContent;

}
