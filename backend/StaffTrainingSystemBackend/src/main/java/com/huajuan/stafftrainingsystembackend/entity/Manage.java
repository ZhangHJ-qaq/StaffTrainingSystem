package com.huajuan.stafftrainingsystembackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "manage")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Manage {
    @Column(name = "dept_id")
    @Id
    private int deptID;

    @Column(name = "dept_manager_id")
    private String deptManagerID;
}
