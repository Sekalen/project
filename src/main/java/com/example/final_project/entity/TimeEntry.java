package com.example.final_project.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "time_entries")
@Data
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student student;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    private String description;

    private LocalDateTime start;

    private LocalDateTime endTime;

    private Boolean isBillable = true;  
}