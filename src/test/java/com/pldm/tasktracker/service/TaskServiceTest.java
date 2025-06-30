package com.pldm.tasktracker.service;

import com.pldm.tasktracker.dto.TaskDto;
import com.pldm.tasktracker.model.Employee;
import com.pldm.tasktracker.model.Role;
import com.pldm.tasktracker.model.Task;
import com.pldm.tasktracker.repository.EmployeeRepository;
import com.pldm.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .username("test")
                .password("testpassword")
                .role(Role.USER)
                .build();
        employee = employeeRepository.save(employee);
    }

    @Test
    void createAndFindTask() {
        //arrange
        TaskDto dto = new TaskDto();
        dto.setTitle("Create Task");
        dto.setDescription("Desc");
        dto.setStatus("TODO");
        dto.setEmployeeId(employee.getId());

        //act
        TaskDto created = taskService.createTask(dto);
        Optional<Task> found = taskRepository.findById(created.getId());

        //assert
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Create Task");
        assertThat(found.get().getEmployee().getId()).isEqualTo(employee.getId());
    }

    @Test
    void updateTaskStatus() throws ChangeSetPersister.NotFoundException {
        //arrange
        TaskDto dto = new TaskDto();
        dto.setTitle("Update Task");
        dto.setDescription("Desc");
        dto.setStatus("TODO");
        dto.setEmployeeId(employee.getId());
        TaskDto created = taskService.createTask(dto);

        //act
        TaskDto updated = taskService.updateStatus(created.getId(), "IN_PROCESS");

        //assert
        assertThat(updated.getStatus()).isEqualTo("IN_PROCESS");
    }

    @Test
    void filterTasksByStatus() {
        //arrange
        TaskDto dto1 = new TaskDto();
        dto1.setTitle("Task1");
        dto1.setStatus("TODO");
        dto1.setEmployeeId(employee.getId());
        taskService.createTask(dto1);

        TaskDto dto2 = new TaskDto();
        dto2.setTitle("Task2");
        dto2.setStatus("DONE");
        dto2.setEmployeeId(employee.getId());
        taskService.createTask(dto2);

        //act
        List<TaskDto> todos = taskService.filterTasks("TODO", null);

        //assert
        assertThat(todos).anyMatch(t -> t.getTitle().equals("Task1"));
        assertThat(todos).noneMatch(t -> t.getTitle().equals("Task2"));
    }
}
