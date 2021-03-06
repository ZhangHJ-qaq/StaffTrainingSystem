package com.huajuan.stafftrainingsystembackend.repository;

import com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO;
import com.huajuan.stafftrainingsystembackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    public List<Employee> findEmployeeByName(String name);

    public Employee findEmployeeByEmployeeID(String employeeID);

    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO(e.employeeID, e.name, e.password, e.gender, e.arrivalTime, e.email, e.phoneNumber, e.role, d.deptName,e.deptID)" +
            " from Employee e left join Department d on d.deptID = e.deptID")
    public List<EmployeeDTO> findAllEmployeeDTO();

    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO(e.employeeID, e.name, e.password, e.gender, e.arrivalTime, e.email, e.phoneNumber, e.role, d.deptName,e.deptID)" +
            " from Employee e left join Department d on d.deptID = e.deptID where e.deptID=:deptID order by e.role asc ")
    public List<EmployeeDTO> findAllEmployeeDTOWithDeptID(@Param("deptID") Integer deptID);

    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO(e.employeeID, e.name, e.password, e.gender, e.arrivalTime, e.email, e.phoneNumber, e.role, d.deptName,e.deptID)" +
            " from Employee e left join Department d on d.deptID = e.deptID where e.name=:name")
    public List<EmployeeDTO> findAllEmployeeDTOWithoutScoreWithName(@Param("name") String name);

    @Query(value = "select new com.huajuan.stafftrainingsystembackend.dto.EmployeeDTO(e.employeeID, e.name, e.password, e.gender, e.arrivalTime, e.email, e.phoneNumber, e.role, d.deptName,e.deptID)" +
            " from Employee e left join Department d on d.deptID = e.deptID where e.employeeID=:employeeID")
    public EmployeeDTO findEmployeeDTOWithoutScoreWithEmployeeID(@Param("employeeID") String employeeID);


}
