package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.entity.Manage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface ManageRepository extends JpaRepository<Manage, Integer> {

    public List<Manage> findAllByDeptID(Integer deptID);
}
