package com.pldm.tasktracker.service;

import com.pldm.tasktracker.dto.TaskDto;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    TaskDto getTaskById(Long id) throws ChangeSetPersister.NotFoundException;
    void deleteTask(Long id);
    TaskDto updateStatus(Long id, String status) throws ChangeSetPersister.NotFoundException;
    List<TaskDto> filterTasks(String status, Long employeeId);
}
