package com.huajuan.stafftrainingsystembackend.request.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentModifySelfInfoRequest {

    private String name;
    private String gender;
    private String email;
    private String phoneNumber;

}
