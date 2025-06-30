package com.pldm.tasktracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pldm.tasktracker.dto.EmployeeDto;
import com.pldm.tasktracker.model.Role;
import com.pldm.tasktracker.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registerEmployee() throws Exception {
        //arrange
        EmployeeDto dto = new EmployeeDto();
        dto.setUsername("test");
        dto.setPassword("password123");
        dto.setRole(Role.USER);

        //act
        mockMvc.perform(post("/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee added"));
        //assert
        assertThat(employeeRepository.findEmployeeByUsername("test")).isPresent();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldGetEmployeeByUsername() throws Exception {
        //arrange
        EmployeeDto dto = new EmployeeDto();
        dto.setUsername("testuser2");
        dto.setPassword("password123");
        dto.setRole(Role.USER);

        mockMvc.perform(post("/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        
        String userLoginJson = """
                    {"username":"testuser2","password":"password123"}
                """;
        String userAuthResponse = mockMvc.perform(post("/auth/sing-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String userToken = objectMapper.readTree(userAuthResponse).get("token").asText();

        //act assert
        mockMvc.perform(get("/employee/username/testuser2")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser2"));
    }


}
