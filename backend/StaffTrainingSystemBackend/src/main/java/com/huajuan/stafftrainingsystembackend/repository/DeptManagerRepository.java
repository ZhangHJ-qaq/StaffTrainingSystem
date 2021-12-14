package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.entity.DeptManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptManagerRepository extends JpaRepository<DeptManager, String> {
}
