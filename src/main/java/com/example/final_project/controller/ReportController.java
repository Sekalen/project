package com.example.final_project.controller;

import com.example.final_project.entity.TimeEntry;
import com.example.final_project.repository.TimeEntryRepository;
import com.example.final_project.repository.NormRepository;
import com.example.final_project.entity.Norm;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private NormRepository normRepository;

    @GetMapping("/export")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<byte[]> exportReport() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Time Report");

            // Заголовки
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Student ID");
            header.createCell(1).setCellValue("Task Type");
            header.createCell(2).setCellValue("Hours");
            header.createCell(3).setCellValue("Date");

            // Данные из БД
            List<TimeEntry> entries = timeEntryRepository.findAll();
            int rowNum = 1;
            for (TimeEntry entry : entries) {
                if (entry.getEndTime() == null) continue; // только завершённые

                // Считаем часы с десятичной дробью (например, 0.5 для 30 минут)
                double minutes = ChronoUnit.MINUTES.between(entry.getStart(), entry.getEndTime());
                double hours = minutes / 60.0;

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getStudent().getId());
                row.createCell(1).setCellValue(entry.getType().toString());
                row.createCell(2).setCellValue(hours);
                row.createCell(3).setCellValue(entry.getStart().toString());
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bos.toByteArray());
        }
    }

    @PostMapping(value = "/upload-norms", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String uploadNorms(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        String[] lines = content.split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String taskType = parts[0].trim();
                double hours = Double.parseDouble(parts[1].trim());
                Norm norm = new Norm();
                norm.setTaskType(taskType);
                norm.setHours(hours);
                normRepository.save(norm);
                System.out.println("Норма для " + taskType + ": " + hours + " часов");
            }
        }
        return "Нормы обновлены";
    }
}