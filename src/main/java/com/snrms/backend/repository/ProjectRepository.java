package com.snrms.backend.repository;

import com.snrms.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Powers GET /api/Projects/search?name=... used by "searchProjects"
    // in projectService.js (called from PublicProjects.jsx).
    List<Project> findByProjectNameContainingIgnoreCase(String name);

    boolean existsByResource_ResourceID(Long resourceId);
}
