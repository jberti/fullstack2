package br.com.jtech.tasklist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TasklistResponse {
    private UUID id;
    private String name;
    private String description;
    private String color;
    private Long taskCount;
    private Long completedTaskCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}