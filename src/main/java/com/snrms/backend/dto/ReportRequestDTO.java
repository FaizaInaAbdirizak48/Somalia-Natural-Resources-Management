package com.snrms.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

/**
 * Matches Reports.jsx's formData exactly:
 *   { projectID, generatedByID, reportType, reportDate, summary }
 */
@Data
public class ReportRequestDTO {

    @NotNull(message = "Project is required")
    private Long projectID;

    @NotNull(message = "The user who generated this report is required")
    private Long generatedByID;

    @Pattern(regexp = "Progress|Financial|Environmental",
             message = "Report type must be one of: Progress, Financial, Environmental")
    private String reportType = "Progress";

    @NotNull(message = "Report date is required")
    private LocalDate reportDate;

    private String summary;
}
