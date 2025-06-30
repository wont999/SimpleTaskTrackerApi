package com.pldm.tasktracker.dto;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long employeeId;
    private String employeeUsername;
    private String createdAt;
    private String updatedAt;
}
