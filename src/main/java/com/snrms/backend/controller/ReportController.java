package com.snrms.backend.controller;

import com.snrms.backend.dto.ReportDTO;
import com.snrms.backend.dto.ReportRequestDTO;
import com.snrms.backend.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Matches reportService.js exactly:
 *   getAllReports  -> GET    /api/Reports
 *   getReportById  -> GET    /api/Reports/{id}
 *   createReport   -> POST   /api/Reports
 *   updateReport   -> PUT    /api/Reports/{id}
 *   deleteReport   -> DELETE /api/Reports/{id}
 *   searchReports  -> GET    /api/Reports/search?type=...
 */
@RestController
@RequestMapping("/api/Reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReportDTO>> searchReports(@RequestParam String type) {
        return ResponseEntity.ok(reportService.searchReports(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @PostMapping
    public ResponseEntity<ReportDTO> createReport(@Valid @RequestBody ReportRequestDTO dto) {
        ReportDTO created = reportService.createReport(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable Long id, @Valid @RequestBody ReportRequestDTO dto) {
        return ResponseEntity.ok(reportService.updateReport(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
