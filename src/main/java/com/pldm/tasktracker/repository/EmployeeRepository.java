package com.pldm.tasktracker.repository;

import com.pldm.tasktracker.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findEmployeeByUsername(String username);
    Optional<Employee> findEmployeeById(Long id);
}
