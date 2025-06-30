package com.pldm.tasktracker.controller;

import com.pldm.tasktracker.dto.JwtAuthenticationDto;
import com.pldm.tasktracker.dto.RefreshTokenDto;
import com.pldm.tasktracker.dto.UserCredentialsDto;
import com.pldm.tasktracker.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final EmployeeServiceImpl employeeService;
    
    @PostMapping("/sing-in")
    public ResponseEntity<JwtAuthenticationDto> singIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = employeeService.singIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed" + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return employeeService.refreshToken(refreshTokenDto);
    }
}
