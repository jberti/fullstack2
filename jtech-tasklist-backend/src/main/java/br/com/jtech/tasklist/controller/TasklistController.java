package br.com.jtech.tasklist.controller;

import br.com.jtech.tasklist.dto.TasklistRequest;
import br.com.jtech.tasklist.dto.TasklistResponse;
import br.com.jtech.tasklist.service.TasklistService;
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
@RequestMapping("/api/v1/tasklists")
@RequiredArgsConstructor
@Tag(name = "Tasklists", description = "Gerenciamento de listas de tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TasklistController {

    private final TasklistService tasklistService;

    @GetMapping
    @Operation(summary = "Listar todas as listas do usuário")
    public ResponseEntity<List<TasklistResponse>> getTasklists(Authentication authentication) {
        UUID userId = getUserId(authentication);
        List<TasklistResponse> tasklists = tasklistService.findByUserId(userId);
        return ResponseEntity.ok(tasklists);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lista por ID")
    public ResponseEntity<TasklistResponse> getTasklist(@PathVariable UUID id, Authentication authentication) {
        UUID userId = getUserId(authentication);
        TasklistResponse tasklist = tasklistService.findByIdAndUserId(id, userId);
        return ResponseEntity.ok(tasklist);
    }

    @PostMapping
    @Operation(summary = "Criar nova lista de tarefas")
    public ResponseEntity<TasklistResponse> createTasklist(
            @Valid @RequestBody TasklistRequest request,
            Authentication authentication) {
        UUID userId = getUserId(authentication);
        TasklistResponse tasklist = tasklistService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tasklist);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lista de tarefas")
    public ResponseEntity<TasklistResponse> updateTasklist(
            @PathVariable UUID id,
            @Valid @RequestBody TasklistRequest request,
            Authentication authentication) {
        UUID userId = getUserId(authentication);
        TasklistResponse tasklist = tasklistService.update(id, request, userId);
        return ResponseEntity.ok(tasklist);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lista de tarefas")
    public ResponseEntity<Void> deleteTasklist(@PathVariable UUID id, Authentication authentication) {
        UUID userId = getUserId(authentication);
        tasklistService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    private UUID getUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}