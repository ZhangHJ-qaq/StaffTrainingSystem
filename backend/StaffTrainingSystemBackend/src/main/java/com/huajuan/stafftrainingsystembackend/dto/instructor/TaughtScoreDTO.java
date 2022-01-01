package com.huajuan.stafftrainingsystembackend.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用来显示给教员的，他教的学生的成绩信息
 * 在/instructor/my_students中显示学生的成绩时使用
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TaughtScoreDTO {

    private Long participateID;
    private String studentID;
    private String name;
    private Date startDate;
    private Boolean finished;
    private Integer score;

    public boolean getPassed() {
        return score != null && score >= 60;
    }
}
