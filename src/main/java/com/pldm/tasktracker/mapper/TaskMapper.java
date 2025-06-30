package com.pldm.tasktracker.mapper;

import com.pldm.tasktracker.dto.TaskDto;
import com.pldm.tasktracker.model.Task;
import com.pldm.tasktracker.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public TaskMapper(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public TaskDto toDto(Task task){
        if (task == null) return null;
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        if(task.getEmployee()!=null){
            dto.setEmployeeId(task.getEmployee().getId());
            dto.setEmployeeUsername(task.getEmployee().getUsername());
        }
        dto.setCreatedAt(task.getCreatedAt() != null ? task.getCreatedAt().toString() : null);
        dto.setUpdatedAt(task.getUpdatedAt() != null ? task.getUpdatedAt().toString() : null);
        return dto;
    }

    public Task toEntity(TaskDto dto) {
        if (dto == null) return null;
        Task.TaskBuilder builder = Task.builder();
        builder.id(dto.getId());
        builder.title(dto.getTitle());
        builder.description(dto.getDescription());
        if (dto.getStatus() != null) {
            builder.status(com.pldm.tasktracker.model.TaskStatus.valueOf(dto.getStatus()));
        }
        if (dto.getEmployeeId() != null) {
            builder.employee(employeeRepository.findById(dto.getEmployeeId()).orElse(null));
        }
        if (dto.getCreatedAt() != null) {
            builder.createdAt(java.time.LocalDateTime.parse(dto.getCreatedAt()));
        }
        if (dto.getUpdatedAt() != null) {
            builder.updatedAt(java.time.LocalDateTime.parse(dto.getUpdatedAt()));
        }
        return builder.build();
    }
}
