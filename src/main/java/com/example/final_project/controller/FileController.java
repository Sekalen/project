package com.example.final_project.controller;

import com.example.final_project.entity.Product;
import com.example.final_project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Excel file with products")
    @ApiResponse(responseCode = "200", description = "File uploaded successfully")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                
                Product product = new Product();
                product.setName(row.getCell(0).getStringCellValue());
                product.setPrice(row.getCell(1).getNumericCellValue());
                product.setQuantity((int) row.getCell(2).getNumericCellValue());
                product.setDescription(row.getCell(3).getStringCellValue());
                
                productService.create(product);
            }
        }
        return "Файл успешно загружен и продукты добавлены!";
    }

    @GetMapping("/report")
    @Operation(summary = "Download product report as Excel")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<byte[]> downloadReport() throws IOException {
        List<Product> products = productService.getAll();
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");
            
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Price");
            header.createCell(3).setCellValue("Quantity");
            header.createCell(4).setCellValue("Description");
            
            int rowNum = 1;
            for (Product p : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getPrice());
                row.createCell(3).setCellValue(p.getQuantity());
                row.createCell(4).setCellValue(p.getDescription());
            }
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bos.toByteArray());
        }
    }
}