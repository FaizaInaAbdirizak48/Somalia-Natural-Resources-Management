package com.snrms.backend.service;

import com.snrms.backend.dto.CategoryDTO;
import com.snrms.backend.entity.Category;
import com.snrms.backend.exception.DuplicateResourceException;
import com.snrms.backend.exception.ResourceInUseException;
import com.snrms.backend.exception.ResourceNotFoundException;
import com.snrms.backend.mapper.CategoryMapper;
import com.snrms.backend.repository.CategoryRepository;
import com.snrms.backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * All the business logic for categories lives here - not in the controller.
 * The controller's job is just to receive HTTP requests and delegate to us;
 * this class doesn't know or care about HTTP at all.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ResourceRepository resourceRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = findCategoryOrThrow(id);
        return categoryMapper.toDTO(category);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName())) {
            throw new DuplicateResourceException(
                    "A category named '" + dto.getCategoryName() + "' already exists.");
        }
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category category = findCategoryOrThrow(id);

        // Only enforce uniqueness if the name is actually changing.
        boolean nameChanged = !category.getCategoryName().equalsIgnoreCase(dto.getCategoryName());
        if (nameChanged && categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName())) {
            throw new DuplicateResourceException(
                    "A category named '" + dto.getCategoryName() + "' already exists.");
        }

        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = findCategoryOrThrow(id);

        if (resourceRepository.existsByCategory_CategoryID(id)) {
            throw new ResourceInUseException(
                    "Cannot delete '" + category.getCategoryName() + "': it is still assigned to one or more resources.");
        }

        categoryRepository.delete(category);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
