package com.example.final_project.dto;

import com.example.final_project.entity.Student;
import com.example.final_project.entity.TaskType;
import lombok.Data;

@Data
public class TimeEntryRequest {
    private Student student;
    private TaskType type;
    private String description;
    private boolean isBillable;
}