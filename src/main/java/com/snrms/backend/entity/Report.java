package com.snrms.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a row in the "reports" table.
 * Matches Reports.jsx: projectID (FK), generatedByID (FK to User),
 * reportType, reportDate, summary.
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by_id", nullable = false)
    private User generatedBy;

    // Progress | Financial | Environmental
    @Column(nullable = false)
    private String reportType;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Column(columnDefinition = "TEXT")
    private String summary;
}
