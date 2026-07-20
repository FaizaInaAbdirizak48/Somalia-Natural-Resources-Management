package com.snrms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * What we send back for a report. Both Dashboard.jsx and Reports.jsx read:
 *   row.project?.projectName
 *   row.generatedBy?.fullName
 * so both nested objects must be present in the response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long reportID;
    private Long projectID;
    private ProjectDTO project;
    private Long generatedByID;
    private UserDTO generatedBy;
    private String reportType;
    private LocalDate reportDate;
    private String summary;
}
