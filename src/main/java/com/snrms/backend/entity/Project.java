package com.snrms.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a row in the "projects" table.
 * Matches Projects.jsx: projectName, resourceID (FK), companyName,
 * startDate, endDate, status, description.
 */
@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectID;

    @Column(nullable = false)
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    private String companyName;

    private LocalDate startDate;

    private LocalDate endDate;

    // Planned | Ongoing | Completed | Suspended
    @Column(nullable = false)
    private String status = "Planned";

    @Column(columnDefinition = "TEXT")
    private String description;
}
