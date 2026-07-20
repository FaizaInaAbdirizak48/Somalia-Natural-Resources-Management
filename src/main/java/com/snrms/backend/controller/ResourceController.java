package com.snrms.backend.controller;

import com.snrms.backend.dto.ResourceDTO;
import com.snrms.backend.dto.ResourceRequestDTO;
import com.snrms.backend.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Matches resourceService.js exactly:
 *   getAllResources  -> GET    /api/Resources
 *   getResourceById  -> GET    /api/Resources/{id}
 *   createResource   -> POST   /api/Resources
 *   updateResource   -> PUT    /api/Resources/{id}
 *   deleteResource   -> DELETE /api/Resources/{id}
 *   searchResources  -> GET    /api/Resources/search?name=...
 */
@RestController
@RequestMapping("/api/Resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    // IMPORTANT: this must be declared BEFORE /{id} so Spring doesn't try
    // to parse "search" as a numeric resourceID.
    @GetMapping("/search")
    public ResponseEntity<List<ResourceDTO>> searchResources(@RequestParam String name) {
        return ResponseEntity.ok(resourceService.searchResources(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> getResourceById(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @PostMapping
    public ResponseEntity<ResourceDTO> createResource(@Valid @RequestBody ResourceRequestDTO dto) {
        ResourceDTO created = resourceService.createResource(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDTO> updateResource(@PathVariable Long id, @Valid @RequestBody ResourceRequestDTO dto) {
        return ResponseEntity.ok(resourceService.updateResource(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
