package dev.guillermo.tasks.dto;

import dev.guillermo.tasks.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequest(
        @NotBlank @Size(max = 120) String title,
        @Size(max = 1000) String description,
        TaskStatus status,
        LocalDate dueDate) {}
