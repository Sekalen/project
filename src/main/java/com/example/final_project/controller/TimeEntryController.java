package com.example.final_project.controller;

import com.example.final_project.dto.TimeEntryRequest;
import com.example.final_project.entity.TimeEntry;
import com.example.final_project.service.TimeEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/time")
public class TimeEntryController {

    @Autowired
    private TimeEntryService timeEntryService;

    @PostMapping("/start")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<TimeEntry> startTime(@RequestBody TimeEntryRequest request) {
        TimeEntry entry = new TimeEntry();
        entry.setStudent(request.getStudent());
        entry.setType(request.getType());
        entry.setDescription(request.getDescription());
        entry.setIsBillable(request.isBillable());
        return ResponseEntity.ok(timeEntryService.startTime(entry));
    }

    @PostMapping("/stop/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<TimeEntry> stopTime(@PathVariable Long id) {
        return ResponseEntity.ok(timeEntryService.stopTime(id));
    }

    @GetMapping("/weekly")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TimeEntry>> getWeeklyStats(@RequestParam Long studentId) {
        return ResponseEntity.ok(timeEntryService.getWeeklyStats(studentId));
    }

    @GetMapping("/heatmap")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Integer>> getHeatmap(@RequestParam Long studentId) {
        List<TimeEntry> entries = timeEntryService.getWeeklyStats(studentId);
        Map<String, Integer> heatmap = new java.util.HashMap<>();
        for (TimeEntry entry : entries) {
            String day = entry.getStart().getDayOfWeek().toString();
            heatmap.put(day, heatmap.getOrDefault(day, 0) + 1);
        }
        return ResponseEntity.ok(heatmap);
    }
}