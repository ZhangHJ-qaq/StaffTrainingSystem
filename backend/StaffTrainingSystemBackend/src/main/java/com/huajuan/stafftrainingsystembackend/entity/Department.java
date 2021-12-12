package com.huajuan.stafftrainingsystembackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "department")
public class Department {

    /**
     * 部门编号
     */
    @Id
    @Column(name = "dept_id")
    private String deptID;

    /**
     * 部门名字
     */
    @Column(name = "dept_name")
    private String deptName;
}
