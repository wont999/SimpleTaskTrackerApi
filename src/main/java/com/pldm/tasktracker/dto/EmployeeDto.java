package com.pldm.tasktracker.dto;

import com.pldm.tasktracker.model.Role;
import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;
    private String username;
    private Role role;
    private String password;
}
