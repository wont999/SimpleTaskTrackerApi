package com.pldm.tasktracker.service;

import com.pldm.tasktracker.dto.TaskDto;
import com.pldm.tasktracker.mapper.TaskMapper;
import com.pldm.tasktracker.model.Task;
import com.pldm.tasktracker.model.TaskStatus;
import com.pldm.tasktracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task saved = taskRepository.save(task);
        return taskMapper.toDto(saved);
    }

    @Override
    public TaskDto getTaskById(Long id) throws ChangeSetPersister.NotFoundException {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TaskDto updateStatus(Long id, String status) throws ChangeSetPersister.NotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        task.setStatus(TaskStatus.valueOf(status));
        Task updated = taskRepository.save(task);
        return taskMapper.toDto(updated);
    }

    @Override
    public List<TaskDto> filterTasks(String status, Long employeeId) {
        List<Task> tasks;
        if (status != null && employeeId != null) {
            tasks = taskRepository.findByStatusAndEmployeeId(TaskStatus.valueOf(status), employeeId);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(TaskStatus.valueOf(status));
        }else if (employeeId != null){
            tasks= taskRepository.findByEmployeeId(employeeId);
        }else {
            tasks=taskRepository.findAll();
        }
        return tasks.stream().map(taskMapper::toDto).collect(Collectors.toList());
    }
}
