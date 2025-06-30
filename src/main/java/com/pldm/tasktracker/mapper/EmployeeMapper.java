package com.pldm.tasktracker.mapper;

import com.pldm.tasktracker.dto.EmployeeDto;
import com.pldm.tasktracker.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeDto toDto(Employee employee){
        if (employee == null) return null;
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setUsername(employee.getUsername());
        dto.setRole(employee.getRole());
        dto.setPassword(null);
        return dto;
    }

    public Employee toEntity(EmployeeDto employeeDto) {
        if (employeeDto == null) return null;
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setUsername(employeeDto.getUsername());
        employee.setRole(employeeDto.getRole());
        employee.setPassword(employeeDto.getPassword());
        employee.setTasks(null);
        return employee;
    }
}
