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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dept_manager")
public class DeptManager {

    @Id
    @Column(name = "dept_manager_id")
    private String deptManagerID;
}
