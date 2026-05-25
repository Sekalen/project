package com.example.final_project.controller;

import com.example.final_project.dto.DeadlinePredictionDTO;
import com.example.final_project.entity.Norm;
import com.example.final_project.entity.TaskType;
import com.example.final_project.entity.TimeEntry;
import com.example.final_project.repository.NormRepository;
import com.example.final_project.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private NormRepository normRepository;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @GetMapping("/deadline")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public DeadlinePredictionDTO getDeadlinePrediction(
            @RequestParam Long studentId,
            @RequestParam String taskType
    ) {
        // 1. Проверяем, существует ли такой тип задачи
        TaskType type;
        try {
            type = TaskType.valueOf(taskType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Неверный тип задачи: " + taskType);
        }

        // 2. Получаем все завершённые записи студента по этому типу
        List<TimeEntry> entries = timeEntryRepository
                .findByStudentIdAndTypeAndEndTimeIsNotNull(studentId, type);

        // 3. Считаем общее потраченное время (в часах)
        double spentHours = entries.stream()
                .mapToDouble(e -> ChronoUnit.HOURS.between(e.getStart(), e.getEndTime()))
                .sum();

        // 4. Получаем норму из БД
        Optional<Norm> normOpt = normRepository.findByTaskType(taskType.toUpperCase());
        double normHours = normOpt.map(Norm::getHours).orElse(10.0);

        // 5. Вычисляем оставшееся время
        double remaining = normHours - spentHours;

        // 6. Определяем риск
        String risk;
        if (remaining > 5) {
            risk = "LOW";
        } else if (remaining > 0) {
            risk = "MEDIUM";
        } else {
            risk = "HIGH";
        }

        // 7. Формируем ответ
        DeadlinePredictionDTO dto = new DeadlinePredictionDTO();
        dto.setSubject(taskType);
        dto.setDeadline(LocalDateTime.now().plusDays(7));
        dto.setHoursLeft(remaining);
        dto.setRisk(risk);
        return dto;
    }
}