package com.snrms.backend.repository;

import com.snrms.backend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // Powers GET /api/Reports/search?type=... used by "searchReports" in
    // reportService.js.
    List<Report> findByReportTypeContainingIgnoreCase(String type);

    boolean existsByProject_ProjectID(Long projectId);

    boolean existsByGeneratedBy_UserID(Long userId);
}
