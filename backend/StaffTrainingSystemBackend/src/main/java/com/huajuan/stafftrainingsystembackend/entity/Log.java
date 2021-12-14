package com.huajuan.stafftrainingsystembackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "log")
public class Log {

    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private Date time;

    @Column(name = "operator")
    private String operator;
}
