package com.pldm.tasktracker.controller;

import com.pldm.tasktracker.dto.TaskDto;
import com.pldm.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/create")
    public TaskDto createTask(@RequestBody TaskDto taskDto){
        return taskService.createTask(taskDto);
    }

    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return taskService.getTaskById(id);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}/status")
    public TaskDto updateStatus(@PathVariable Long id, @RequestParam String status) throws ChangeSetPersister.NotFoundException {
        return taskService.updateStatus(id, status);
    }

    @GetMapping
    public List<TaskDto> filterTasks(@RequestParam(required = false) String status,
                                     @RequestParam(required = false) Long employeeId){
        return taskService.filterTasks(status, employeeId);
    }


}
