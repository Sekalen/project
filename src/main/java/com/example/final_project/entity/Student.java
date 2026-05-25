package com.example.final_project.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private List<TimeEntry> recentEntries;
}