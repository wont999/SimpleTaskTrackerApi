package com.pldm.tasktracker.security;

import com.pldm.tasktracker.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findEmployeeByUsername(username).map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
