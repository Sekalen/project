package com.example.final_project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "norms")
@Data
public class Norm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String taskType;

    @Column(nullable = false)
    private Double hours;
}