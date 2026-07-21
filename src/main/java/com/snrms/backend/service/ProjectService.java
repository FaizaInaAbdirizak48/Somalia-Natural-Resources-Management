package com.snrms.backend.service;

import com.snrms.backend.dto.ProjectDTO;
import com.snrms.backend.dto.ProjectRequestDTO;
import com.snrms.backend.entity.Project;
import com.snrms.backend.entity.Resource;
import com.snrms.backend.exception.ResourceInUseException;
import com.snrms.backend.exception.ResourceNotFoundException;
import com.snrms.backend.mapper.ProjectMapper;
import com.snrms.backend.repository.ProjectRepository;
import com.snrms.backend.repository.ReportRepository;
import com.snrms.backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ResourceRepository resourceRepository;
    private final ReportRepository reportRepository;
    private final ProjectMapper projectMapper;

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public ProjectDTO getProjectById(Long id) {
        return projectMapper.toDTO(findProjectOrThrow(id));
    }

    // Powers "searchProjects" - used both by the admin Projects page (planned)
    // and PublicProjects.jsx's search box.
    public List<ProjectDTO> searchProjects(String name) {
        return projectRepository.findByProjectNameContainingIgnoreCase(name).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    @Transactional
    public ProjectDTO createProject(ProjectRequestDTO dto) {
        Project project = new Project();
        applyRequestToEntity(project, dto);
        Project saved = projectRepository.save(project);
        return projectMapper.toDTO(saved);
    }

    @Transactional
    public ProjectDTO updateProject(Long id, ProjectRequestDTO dto) {
        Project project = findProjectOrThrow(id);
        applyRequestToEntity(project, dto);
        Project saved = projectRepository.save(project);
        return projectMapper.toDTO(saved);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = findProjectOrThrow(id);

        if (reportRepository.existsByProject_ProjectID(id)) {
            throw new ResourceInUseException(
                    "Cannot delete '" + project.getProjectName() + "': it still has reports associated with it.");
        }

        projectRepository.delete(project);
    }

    private void applyRequestToEntity(Project project, ProjectRequestDTO dto) {
        Resource resource = resourceRepository.findById(dto.getResourceID())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + dto.getResourceID()));

        project.setProjectName(dto.getProjectName());
        project.setResource(resource);
        project.setCompanyName(dto.getCompanyName());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(dto.getStatus() != null ? dto.getStatus() : "Planned");
        project.setDescription(dto.getDescription());
    }

    private Project findProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }
}
