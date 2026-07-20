package com.snrms.backend.mapper;

import com.snrms.backend.dto.CategoryDTO;
import com.snrms.backend.entity.Category;
import org.springframework.stereotype.Component;

/**
 * Converts between the Category entity (database row) and CategoryDTO
 * (what the API sends/receives). Kept as plain, explicit Java - no "magic"
 * library - so it's easy to follow as a beginner and easy to debug.
 */
@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(category.getCategoryID(), category.getCategoryName(), category.getDescription());
    }

    public Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        return category;
    }
}
