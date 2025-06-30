package com.pldm.tasktracker.service;

import com.pldm.tasktracker.dto.EmployeeDto;
import com.pldm.tasktracker.dto.JwtAuthenticationDto;
import com.pldm.tasktracker.dto.RefreshTokenDto;
import com.pldm.tasktracker.dto.UserCredentialsDto;
import org.springframework.data.crossstore.ChangeSetPersister;

import javax.naming.AuthenticationException;
import javax.swing.text.ChangedCharSetException;

public interface EmployeeService {
    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;

    EmployeeDto getEmployeeById(String id) throws ChangeSetPersister.NotFoundException;
    EmployeeDto getEmployeeByUsername(String username) throws ChangeSetPersister.NotFoundException;

    String addEmployee(EmployeeDto employee);
}
