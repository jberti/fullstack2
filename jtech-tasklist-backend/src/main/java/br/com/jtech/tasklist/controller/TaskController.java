package br.com.jtech.tasklist.controller;

import br.com.jtech.tasklist.dto.TaskRequest;
import br.com.jtech.tasklist.dto.TaskResponse;
import br.com.jtech.tasklist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Gerenciamento de tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    @Operation(summary = "Listar todas as tarefas do usuário")
    public ResponseEntity<List<TaskResponse>> getTasks(Authentication authentication) {
        UUID userId = getUserId(authentication);
        List<TaskResponse> tasks = taskService.findByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasklists/{tasklistId}/tasks")
    @Operation(summary = "Listar tarefas de uma lista específica")
    public ResponseEntity<List<TaskResponse>> getTasksByTasklist(
            @PathVariable UUID tasklistId,
            Authentication authentication) {
        UUID userId = getUserId(authentication);
        List<TaskResponse> tasks = taskService.findByTasklistId(tasklistId, userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "Buscar tarefa por ID")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id, Authentication authentication) {
        UUID userId = getUserId(authentication);
        TaskResponse task = taskService.findByIdAndUserId(id, userId);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/tasks")
    @Operation(summary = "Criar nova tarefa")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        UUID userId = getUserId(authentication);
        TaskResponse task = taskService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Atualizar tarefa")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        UUID userId = getUserId(authentication);
        TaskResponse task = taskService.update(id, request, userId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Excluir tarefa")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id, Authentication authentication) {
        UUID userId = getUserId(authentication);
        taskService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    private UUID getUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}