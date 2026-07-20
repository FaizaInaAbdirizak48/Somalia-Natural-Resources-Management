package com.snrms.backend.service;

import com.snrms.backend.dto.ReportDTO;
import com.snrms.backend.dto.ReportRequestDTO;
import com.snrms.backend.entity.Project;
import com.snrms.backend.entity.Report;
import com.snrms.backend.entity.User;
import com.snrms.backend.exception.ResourceNotFoundException;
import com.snrms.backend.mapper.ReportMapper;
import com.snrms.backend.repository.ProjectRepository;
import com.snrms.backend.repository.ReportRepository;
import com.snrms.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    public ReportDTO getReportById(Long id) {
        return reportMapper.toDTO(findReportOrThrow(id));
    }

    // Powers "searchReports" (by reportType) used by reportService.js.
    public List<ReportDTO> searchReports(String type) {
        return reportRepository.findByReportTypeContainingIgnoreCase(type).stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    @Transactional
    public ReportDTO createReport(ReportRequestDTO dto) {
        Report report = new Report();
        applyRequestToEntity(report, dto);
        Report saved = reportRepository.save(report);
        return reportMapper.toDTO(saved);
    }

    @Transactional
    public ReportDTO updateReport(Long id, ReportRequestDTO dto) {
        Report report = findReportOrThrow(id);
        applyRequestToEntity(report, dto);
        Report saved = reportRepository.save(report);
        return reportMapper.toDTO(saved);
    }

    @Transactional
    public void deleteReport(Long id) {
        Report report = findReportOrThrow(id);
        reportRepository.delete(report);
    }

    private void applyRequestToEntity(Report report, ReportRequestDTO dto) {
        Project project = projectRepository.findById(dto.getProjectID())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + dto.getProjectID()));
        User generatedBy = userRepository.findById(dto.getGeneratedByID())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getGeneratedByID()));

        report.setProject(project);
        report.setGeneratedBy(generatedBy);
        report.setReportType(dto.getReportType() != null ? dto.getReportType() : "Progress");
        report.setReportDate(dto.getReportDate());
        report.setSummary(dto.getSummary());
    }

    private Report findReportOrThrow(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }
}
