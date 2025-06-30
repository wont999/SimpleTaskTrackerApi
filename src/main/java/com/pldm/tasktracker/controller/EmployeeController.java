package com.pldm.tasktracker.controller;

import com.pldm.tasktracker.dto.EmployeeDto;
import com.pldm.tasktracker.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createEmployee(@RequestBody EmployeeDto employeeDto){
        return employeeService.addEmployee(employeeDto);
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmployeeById(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/username/{username}")
    public EmployeeDto getEmployeeByUsername(@PathVariable String username) throws ChangeSetPersister.NotFoundException {
        return employeeService.getEmployeeByUsername(username);
    }
}
