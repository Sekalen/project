package com.example.final_project.controller;

import com.example.final_project.dto.DeadlinePredictionDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @GetMapping("/deadline")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public DeadlinePredictionDTO getDeadlinePrediction(@RequestParam String subject) {
        DeadlinePredictionDTO dto = new DeadlinePredictionDTO();
        dto.setSubject(subject);
        dto.setDeadline(LocalDateTime.now().plusDays(7));
        dto.setHoursLeft(10.0);
        dto.setRisk("MEDIUM");
        return dto;
    }
}