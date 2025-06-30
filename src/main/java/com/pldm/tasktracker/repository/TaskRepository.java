package com.pldm.tasktracker.repository;

import com.pldm.tasktracker.model.Employee;
import com.pldm.tasktracker.model.Task;
import com.pldm.tasktracker.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEmployeeId(Long employeeId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByStatusAndEmployeeId(TaskStatus status, Long employeeId);
}
