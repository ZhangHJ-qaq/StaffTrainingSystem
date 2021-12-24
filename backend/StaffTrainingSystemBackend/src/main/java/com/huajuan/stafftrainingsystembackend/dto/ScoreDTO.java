package com.huajuan.stafftrainingsystembackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用来在员工查询个人信息时，或者管理员查询某个员工的信息时，显示的员工的成绩对象
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {
    private Long participateID;
    private String courseID;
    private String courseName;
    private Date startDate;
    private Boolean finished;
    private String instructorName;
    private Integer score;

}
