package com.huajuan.stafftrainingsystembackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
