package com.snrms.backend.controller;

import com.snrms.backend.dto.ProjectDTO;
import com.snrms.backend.dto.ProjectRequestDTO;
import com.snrms.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Matches projectService.js exactly:
 *   getAllProjects  -> GET    /api/Projects
 *   getProjectById  -> GET    /api/Projects/{id}
 *   createProject   -> POST   /api/Projects
 *   updateProject   -> PUT    /api/Projects/{id}
 *   deleteProject   -> DELETE /api/Projects/{id}
 *   searchProjects  -> GET    /api/Projects/search?name=...
 */
@RestController
@RequestMapping("/api/Projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectDTO>> searchProjects(@RequestParam String name) {
        return ResponseEntity.ok(projectService.searchProjects(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectDTO created = projectService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
