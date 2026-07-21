package com.snrms.backend.service;

import com.snrms.backend.dto.ResourceDTO;
import com.snrms.backend.dto.ResourceRequestDTO;
import com.snrms.backend.entity.Category;
import com.snrms.backend.entity.Resource;
import com.snrms.backend.exception.ResourceInUseException;
import com.snrms.backend.exception.ResourceNotFoundException;
import com.snrms.backend.mapper.ResourceMapper;
import com.snrms.backend.repository.CategoryRepository;
import com.snrms.backend.repository.ProjectRepository;
import com.snrms.backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final CategoryRepository categoryRepository;
    private final ProjectRepository projectRepository;
    private final ResourceMapper resourceMapper;

    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(resourceMapper::toDTO)
                .toList();
    }

    public ResourceDTO getResourceById(Long id) {
        return resourceMapper.toDTO(findResourceOrThrow(id));
    }

    // Powers "searchResources" - Resources.jsx calls this on Enter keypress.
    public List<ResourceDTO> searchResources(String name) {
        return resourceRepository.findByResourceNameContainingIgnoreCase(name).stream()
                .map(resourceMapper::toDTO)
                .toList();
    }

    @Transactional
    public ResourceDTO createResource(ResourceRequestDTO dto) {
        Resource resource = new Resource();
        applyRequestToEntity(resource, dto);
        Resource saved = resourceRepository.save(resource);
        return resourceMapper.toDTO(saved);
    }

    @Transactional
    public ResourceDTO updateResource(Long id, ResourceRequestDTO dto) {
        Resource resource = findResourceOrThrow(id);
        applyRequestToEntity(resource, dto);
        Resource saved = resourceRepository.save(resource);
        return resourceMapper.toDTO(saved);
    }

    @Transactional
    public void deleteResource(Long id) {
        Resource resource = findResourceOrThrow(id);

        if (projectRepository.existsByResource_ResourceID(id)) {
            throw new ResourceInUseException(
                    "Cannot delete '" + resource.getResourceName() + "': it is still referenced by one or more projects.");
        }

        resourceRepository.delete(resource);
    }

    private void applyRequestToEntity(Resource resource, ResourceRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryID())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryID()));

        resource.setResourceName(dto.getResourceName());
        resource.setCategory(category);
        resource.setLocation(dto.getLocation());
        resource.setQuantity(dto.getQuantity());
        resource.setUnit(dto.getUnit());
        resource.setStatus(dto.getStatus() != null ? dto.getStatus() : "Available");
    }

    private Resource findResourceOrThrow(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }
}
