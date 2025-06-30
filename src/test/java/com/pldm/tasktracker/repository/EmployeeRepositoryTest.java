package com.pldm.tasktracker.repository;

import com.pldm.tasktracker.model.Employee;
import com.pldm.tasktracker.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void findEmployeeByUsername(){
        //arrange
        Employee employee=Employee.builder()
                .username("test")
                .password("testpassword")
                .role(Role.USER)
                .build();
        entityManager.persistAndFlush(employee);

        //act
        Optional<Employee> found = employeeRepository.findEmployeeByUsername("test");

        //assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test");
        assertThat(found.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void notFindEmployeeByNonExistUsername(){

        //act
        Optional<Employee> found = employeeRepository.findEmployeeByUsername("test");

        //assert
        assertThat(found).isEmpty();
    }

    @Test
    void findEmployeeById(){
        //arrange
        Employee employee = Employee.builder()
                .username("test")
                .password("testpassword")
                .role(Role.MANAGER)
                .build();
        Employee saved = entityManager.persistAndFlush(employee);

        //act
        Optional<Employee> found = employeeRepository.findEmployeeById(saved.getId());

        //assert
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getRole()).isEqualTo(Role.MANAGER);
    }
}
