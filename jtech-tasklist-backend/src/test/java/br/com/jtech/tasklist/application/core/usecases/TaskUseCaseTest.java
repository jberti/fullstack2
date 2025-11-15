/*
*  @(#)TaskUseCaseTest.java
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
package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.TaskOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
* class TaskUseCaseTest 
* 
* @author jtech
*/
@ExtendWith(MockitoExtension.class)
class TaskUseCaseTest {

    @Mock
    private TaskOutputGateway taskOutputGateway;

    @InjectMocks
    private TaskUseCase taskUseCase;

    private Task task;
    private String userId;

    @BeforeEach
    void setUp() {
        userId = "123e4567-e89b-12d3-a456-426614174000";
        task = Task.builder()
            .id("task-123")
            .title("Test Task")
            .description("Test Description")
            .completed(false)
            .userId(userId)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        // Given
        Task newTask = Task.builder()
            .title("New Task")
            .description("New Description")
            .build();
        
        Task savedTask = Task.builder()
            .id("task-456")
            .title(newTask.getTitle())
            .description(newTask.getDescription())
            .completed(false)
            .userId(userId)
            .build();
        
        when(taskOutputGateway.save(any(Task.class))).thenReturn(savedTask);

        // When
        Task result = taskUseCase.create(newTask, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCompleted()).isFalse();
        verify(taskOutputGateway).save(any(Task.class));
    }

    @Test
    void shouldFindAllTasksByUserId() {
        // Given
        List<Task> tasks = Arrays.asList(task);
        when(taskOutputGateway.findByUserId(userId)).thenReturn(tasks);

        // When
        List<Task> result = taskUseCase.findAllByUserId(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        verify(taskOutputGateway).findByUserId(userId);
    }

    @Test
    void shouldFindTaskByIdAndUserId() {
        // Given
        when(taskOutputGateway.findByIdAndUserId(task.getId(), userId))
            .thenReturn(Optional.of(task));

        // When
        Optional<Task> result = taskUseCase.findByIdAndUserId(task.getId(), userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(task.getId());
        assertThat(result.get().getUserId()).isEqualTo(userId);
        verify(taskOutputGateway).findByIdAndUserId(task.getId(), userId);
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        // Given
        Task updatedTask = Task.builder()
            .id(task.getId())
            .title("Updated Title")
            .description("Updated Description")
            .completed(true)
            .build();
        
        when(taskOutputGateway.findByIdAndUserId(task.getId(), userId))
            .thenReturn(Optional.of(task));
        when(taskOutputGateway.save(any(Task.class))).thenReturn(updatedTask);

        // When
        Task result = taskUseCase.update(updatedTask, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(updatedTask.getTitle());
        verify(taskOutputGateway).findByIdAndUserId(task.getId(), userId);
        verify(taskOutputGateway).save(any(Task.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        // Given
        when(taskOutputGateway.findByIdAndUserId(anyString(), anyString()))
            .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> taskUseCase.update(task, userId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Task not found");
        
        verify(taskOutputGateway).findByIdAndUserId(task.getId(), userId);
        verify(taskOutputGateway, never()).save(any(Task.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingTaskOfDifferentUser() {
        // Given
        when(taskOutputGateway.findByIdAndUserId(task.getId(), userId))
            .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> taskUseCase.update(task, userId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Task not found");
        
        verify(taskOutputGateway).findByIdAndUserId(task.getId(), userId);
        verify(taskOutputGateway, never()).save(any(Task.class));
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        // Given
        when(taskOutputGateway.existsByIdAndUserId(task.getId(), userId))
            .thenReturn(true);
        doNothing().when(taskOutputGateway).delete(task.getId());

        // When
        taskUseCase.delete(task.getId(), userId);

        // Then
        verify(taskOutputGateway).existsByIdAndUserId(task.getId(), userId);
        verify(taskOutputGateway).delete(task.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        // Given
        when(taskOutputGateway.existsByIdAndUserId(anyString(), anyString()))
            .thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> taskUseCase.delete(task.getId(), userId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Task not found");
        
        verify(taskOutputGateway).existsByIdAndUserId(task.getId(), userId);
        verify(taskOutputGateway, never()).delete(anyString());
    }
}

