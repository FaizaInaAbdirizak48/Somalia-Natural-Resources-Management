package com.snrms.backend.mapper;

import com.snrms.backend.dto.ReportDTO;
import com.snrms.backend.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportMapper {

    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;

    public ReportDTO toDTO(Report report) {
        if (report == null) return null;
        ReportDTO dto = new ReportDTO();
        dto.setReportID(report.getReportID());
        dto.setProjectID(report.getProject() != null ? report.getProject().getProjectID() : null);
        dto.setProject(projectMapper.toDTO(report.getProject()));
        dto.setGeneratedByID(report.getGeneratedBy() != null ? report.getGeneratedBy().getUserID() : null);
        dto.setGeneratedBy(userMapper.toDTO(report.getGeneratedBy()));
        dto.setReportType(report.getReportType());
        dto.setReportDate(report.getReportDate());
        dto.setSummary(report.getSummary());
        return dto;
    }
}
