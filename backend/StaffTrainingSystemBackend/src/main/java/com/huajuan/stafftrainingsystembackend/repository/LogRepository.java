package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query(value = "select * from log l order by time desc limit 20",nativeQuery = true)
    public List<Log> findAllLimit20();
}
