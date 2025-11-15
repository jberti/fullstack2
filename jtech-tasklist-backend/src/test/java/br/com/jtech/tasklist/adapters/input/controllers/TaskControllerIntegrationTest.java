/*
*  @(#)TaskControllerIntegrationTest.java
*
*  Copyright (c) J-Tech Solucoes em Informatica.
*  All Rights Reserved.
*
*  This software is the confidential and proprietary information of J-Tech.
*  ("Confidential Information"). You shall not disclose such Confidential
*  Information and shall use it only in accordance with the terms of the
*  license agreement you entered into with J-Tech.
*
*/
package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.config.infra.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
* class TaskControllerIntegrationTest 
* 
* @author jtech
*/
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String userId;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // Create a user first
        String registerRequest = """
            {
                "name": "Test User",
                "email": "taskuser@example.com",
                "password": "password123"
            }
            """;

        String response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        // Extract user ID from token
        String authToken = objectMapper.readTree(response).get("token").asText();
        userId = jwtTokenProvider.getUserIdFromToken(authToken);
        token = authToken;
    }

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        String taskRequest = """
            {
                "title": "Test Task",
                "description": "Test Description",
                "completed": false
            }
            """;

        mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskRequest))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Test Task"))
            .andExpect(jsonPath("$.description").value("Test Description"))
            .andExpect(jsonPath("$.completed").value(false))
            .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsMissing() throws Exception {
        String taskRequest = """
            {
                "title": "Test Task",
                "description": "Test Description"
            }
            """;

        int status = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskRequest))
            .andReturn()
            .getResponse()
            .getStatus();
        
        assertThat(status).isIn(401, 403);
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        String invalidRequest = """
            {
                "title": "",
                "description": "Test Description"
            }
            """;

        mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindAllTasksByUserId() throws Exception {
        // First create a task
        String taskRequest = """
            {
                "title": "Task 1",
                "description": "Description 1"
            }
            """;

        mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskRequest))
            .andExpect(status().isCreated());

        // Then get all tasks
        mockMvc.perform(get("/api/v1/tasks")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    void shouldFindTaskById() throws Exception {
        // First create a task
        String taskRequest = """
            {
                "title": "Task to Find",
                "description": "Description"
            }
            """;

        String response = mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskRequest))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String taskId = objectMapper.readTree(response).get("id").asText();

        // Then get task by id
        mockMvc.perform(get("/api/v1/tasks/" + taskId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(taskId))
            .andExpect(jsonPath("$.title").value("Task to Find"));
    }

    @Test
    void shouldUpdateTaskSuccessfully() throws Exception {
        // First create a task
        String createRequest = """
            {
                "title": "Original Title",
                "description": "Original Description"
            }
            """;

        String response = mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequest))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String taskId = objectMapper.readTree(response).get("id").asText();

        // Then update the task
        String updateRequest = """
            {
                "title": "Updated Title",
                "description": "Updated Description",
                "completed": true
            }
            """;

        mockMvc.perform(put("/api/v1/tasks/" + taskId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Updated Title"))
            .andExpect(jsonPath("$.description").value("Updated Description"))
            .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {
        // First create a task
        String createRequest = """
            {
                "title": "Task to Delete",
                "description": "Description"
            }
            """;

        String response = mockMvc.perform(post("/api/v1/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequest))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String taskId = objectMapper.readTree(response).get("id").asText();

        // Then delete the task
        mockMvc.perform(delete("/api/v1/tasks/" + taskId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        // Verify task is deleted
        mockMvc.perform(get("/api/v1/tasks/" + taskId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        String nonExistentId = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/v1/tasks/" + nonExistentId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());
    }
}

