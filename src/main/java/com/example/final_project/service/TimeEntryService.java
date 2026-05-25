package com.example.final_project.service;

import com.example.final_project.entity.TimeEntry;
import com.example.final_project.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeEntryService {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    public TimeEntry startTime(TimeEntry entry) {
        entry.setStart(LocalDateTime.now());
        entry.setEndTime(null);  
        return timeEntryRepository.save(entry);
    }

    public TimeEntry stopTime(Long id) {
        TimeEntry entry = timeEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time entry not found"));
        entry.setEndTime(LocalDateTime.now());  
        return timeEntryRepository.save(entry);
    }

    public List<TimeEntry> getWeeklyStats(Long studentId) {
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        return timeEntryRepository.findByStudentIdAndStartAfter(studentId, weekAgo);
    }
}