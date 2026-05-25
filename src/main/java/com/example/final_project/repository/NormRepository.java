package com.example.final_project.repository;

import com.example.final_project.entity.Norm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NormRepository extends JpaRepository<Norm, Long> {
    Optional<Norm> findByTaskType(String taskType);
}