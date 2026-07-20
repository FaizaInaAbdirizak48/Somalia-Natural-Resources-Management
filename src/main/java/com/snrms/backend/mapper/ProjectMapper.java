package com.snrms.backend.mapper;

import com.snrms.backend.dto.ProjectDTO;
import com.snrms.backend.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ResourceMapper resourceMapper;

    public ProjectDTO toDTO(Project project) {
        if (project == null) return null;
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectID(project.getProjectID());
        dto.setProjectName(project.getProjectName());
        dto.setResourceID(project.getResource() != null ? project.getResource().getResourceID() : null);
        dto.setResource(resourceMapper.toDTO(project.getResource()));
        dto.setCompanyName(project.getCompanyName());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());
        dto.setDescription(project.getDescription());
        return dto;
    }
}
