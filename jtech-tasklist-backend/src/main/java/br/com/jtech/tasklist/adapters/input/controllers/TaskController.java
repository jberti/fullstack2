/*
*  @(#)TaskController.java
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

import br.com.jtech.tasklist.adapters.input.protocols.TaskRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TaskResponse;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.TaskInputGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.com.jtech.tasklist.adapters.input.controllers.TaskController.toResponse;

/**
* class TaskController
* 
* @author jtech
*/
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskInputGateway taskInputGateway;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request, Authentication authentication) {
        String userId = authentication.getName();
        
        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .completed(request.getCompleted())
            .build();
        
        Task createdTask = taskInputGateway.create(task, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(createdTask));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll(Authentication authentication) {
        String userId = authentication.getName();
        List<Task> tasks = taskInputGateway.findAllByUserId(userId);
        List<TaskResponse> responses = tasks.stream()
            .map(TaskController::toResponse)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        Task task = taskInputGateway.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new br.com.jtech.tasklist.config.infra.exceptions.ResourceNotFoundException("Task not found"));
        return ResponseEntity.ok(toResponse(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable String id,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        
        Task task = Task.builder()
            .id(id)
            .title(request.getTitle())
            .description(request.getDescription())
            .completed(request.getCompleted())
            .build();
        
        Task updatedTask = taskInputGateway.update(task, userId);
        return ResponseEntity.ok(toResponse(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        taskInputGateway.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    static TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .completed(task.getCompleted())
            .userId(task.getUserId())
            .createdAt(task.getCreatedAt())
            .updatedAt(task.getUpdatedAt())
            .build();
    }
}

