package com.example.final_project.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeadlinePredictionDTO {
    private String subject;
    private LocalDateTime deadline;
    private Double hoursLeft;
    private String risk; // LOW, MEDIUM, HIGH
}