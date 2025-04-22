package com.iqeq.controller;

import com.iqeq.dto.DocumentResponseDto;
import com.iqeq.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    @GetMapping("/documents")
    public ResponseEntity<Map<String, List<DocumentResponseDto>>> getDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(dashboardService.getDocumentsGrouped(page, size));
    }
    @GetMapping("/documents/{documentType}")
    public ResponseEntity<List<DocumentResponseDto>> getDocumentsByType(
            @PathVariable String documentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(dashboardService.getDocumentsByType(documentType, page, size));
    }




}
