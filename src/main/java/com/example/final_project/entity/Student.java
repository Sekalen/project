package com.example.final_project.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String groupName;  // вместо group

    @OneToMany(mappedBy = "student")
    private List<TimeEntry> recentEntries;
}