package com.snrms.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

/**
 * Matches Projects.jsx's formData exactly:
 *   { projectName, resourceID, companyName, startDate, endDate, status, description }
 */
@Data
public class ProjectRequestDTO {

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotNull(message = "Target resource is required")
    private Long resourceID;

    private String companyName;

    private LocalDate startDate;

    private LocalDate endDate;

    @Pattern(regexp = "Planned|Ongoing|Completed|Suspended",
             message = "Status must be one of: Planned, Ongoing, Completed, Suspended")
    private String status = "Planned";

    private String description;
}
