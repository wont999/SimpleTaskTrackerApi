package com.pldm.tasktracker.service;

import com.pldm.tasktracker.dto.EmployeeDto;
import com.pldm.tasktracker.model.Employee;
import com.pldm.tasktracker.model.Role;
import com.pldm.tasktracker.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void getEmployeeByUsername(){
        //arrange
        EmployeeDto dto=new EmployeeDto();
        dto.setUsername("test");
        dto.setPassword("testpassword");
        dto.setRole(Role.USER);

        //act
        employeeService.addEmployee(dto);
        Optional<Employee> found = employeeRepository.findEmployeeByUsername("test");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test");
        assertThat(found.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void getEmployeeById() throws ChangeSetPersister.NotFoundException {
        //arrange
        EmployeeDto dto=new EmployeeDto();
        dto.setUsername("test");
        dto.setPassword("testpassword");
        dto.setRole(Role.MANAGER);
        employeeService.addEmployee(dto);
        Employee employee = employeeRepository.findEmployeeByUsername("test").orElseThrow();

        //act
        EmployeeDto found = employeeService.getEmployeeById(employee.getId().toString());

        //assert
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("test");
        assertThat(found.getRole()).isEqualTo(Role.MANAGER);
    }
}
