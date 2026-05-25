package com.example.final_project.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @GetMapping("/export")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<byte[]> exportReport() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Time Report");
            
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Student");
            header.createCell(1).setCellValue("Task Type");
            header.createCell(2).setCellValue("Hours");
            header.createCell(3).setCellValue("Date");
            
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("Иванов И.");
            row.createCell(1).setCellValue("LAB");
            row.createCell(2).setCellValue(2.5);
            row.createCell(3).setCellValue(LocalDateTime.now().toString());
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bos.toByteArray());
        }
    }

    @PostMapping("/upload-norms")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String uploadNorms(@RequestParam("file") MultipartFile file) throws IOException {
        // Пример чтения CSV
        String content = new String(file.getBytes());
        String[] lines = content.split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String taskType = parts[0].trim();
                double hours = Double.parseDouble(parts[1].trim());
                // Здесь можно сохранять нормы в БД
                System.out.println("Норма для " + taskType + ": " + hours + " часов");
            }
        }
        return "Нормы обновлены";
    }
}