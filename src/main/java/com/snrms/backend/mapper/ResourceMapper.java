package com.snrms.backend.mapper;

import com.snrms.backend.dto.ResourceDTO;
import com.snrms.backend.entity.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResourceMapper {

    private final CategoryMapper categoryMapper;

    public ResourceDTO toDTO(Resource resource) {
        if (resource == null) return null;
        ResourceDTO dto = new ResourceDTO();
        dto.setResourceID(resource.getResourceID());
        dto.setResourceName(resource.getResourceName());
        dto.setCategoryID(resource.getCategory() != null ? resource.getCategory().getCategoryID() : null);
        dto.setCategory(categoryMapper.toDTO(resource.getCategory()));
        dto.setLocation(resource.getLocation());
        dto.setQuantity(resource.getQuantity());
        dto.setUnit(resource.getUnit());
        dto.setStatus(resource.getStatus());
        return dto;
    }
}
