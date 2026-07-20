package com.snrms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * What we send back for a project. Projects.jsx reads:
 *   project.resource?.category?.categoryName
 *   project.resource?.location
 * so "resource" here must be the full ResourceDTO (which itself nests category).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long projectID;
    private String projectName;
    private Long resourceID;
    private ResourceDTO resource;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String description;
}
