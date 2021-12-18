package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    public Employee findEmployeeByName(String name);

    public Employee findEmployeeByEmployeeID(String employeeID);

    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO(e.employeeID, e.name, e.password, e.gender, e.arrivalTime, e.email, e.phoneNumber, e.role, d.deptName,e.deptID)" +
            " from Employee e left join Department d on d.deptID = e.deptID")
    public List<EmployeeDTO> findAllEmployeeDTO();

}
