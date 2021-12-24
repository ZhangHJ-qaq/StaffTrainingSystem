package com.huajuan.stafftrainingsystembackend.request.instructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterScoreRequest {
    @NotNull(message = "participateID不能为空")
    private Long participateID;

    @Range(min = 0, max = 100, message = "成绩必须在0~100之间")
    @NotNull(message = "成绩不能为空")
    private Integer score;
}
