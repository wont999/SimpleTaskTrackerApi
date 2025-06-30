package com.pldm.tasktracker.service;

import com.pldm.tasktracker.dto.EmployeeDto;
import com.pldm.tasktracker.dto.JwtAuthenticationDto;
import com.pldm.tasktracker.dto.RefreshTokenDto;
import com.pldm.tasktracker.dto.UserCredentialsDto;
import com.pldm.tasktracker.mapper.EmployeeMapper;
import com.pldm.tasktracker.model.Employee;
import com.pldm.tasktracker.model.Role;
import com.pldm.tasktracker.repository.EmployeeRepository;
import com.pldm.tasktracker.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;
    private final JwtService jwtService;
    public Employee register(String username, String password){
        Employee employee = Employee.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        return employeeRepository.save(employee);
    }

    @Override
    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Employee employee = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(employee.getUsername(), employee.getRole());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if(refreshToken != null && jwtService.validateJwtToken(refreshToken)){
            Employee employee = findByUsername(jwtService.getUsernameFromToken(refreshToken));
            return jwtService.refreshBaseToken(employee.getUsername(), refreshToken, employee.getRole());
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    @Override
    public EmployeeDto getEmployeeById(String id) throws ChangeSetPersister.NotFoundException {
        return employeeMapper.toDto(employeeRepository.findEmployeeById(Long.valueOf(id))
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @Override
    public EmployeeDto getEmployeeByUsername(String username) throws ChangeSetPersister.NotFoundException {
        return employeeMapper.toDto(employeeRepository.findEmployeeByUsername(username)
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @Override
    @Transactional
    public String addEmployee(EmployeeDto employeeDto) {
        if(employeeDto.getRole()==null){
            employeeDto.setRole(Role.USER);
        }
        Employee employee = employeeMapper.toEntity(employeeDto);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
        return "Employee added";
    }

    private Employee findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUsername(userCredentialsDto.getUsername());
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            if(passwordEncoder.matches(userCredentialsDto.getPassword(), employee.getPassword())) {
                return employee;
            }
        }
        throw new AuthenticationException("Invalid username or password");

    }

    private Employee findByUsername(String username) throws Exception {
        return employeeRepository.findEmployeeByUsername(username).orElseThrow(() -> new Exception(String.format("Employee with username %s not found", username)));
    }
}
