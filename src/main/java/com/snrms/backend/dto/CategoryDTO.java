package com.snrms.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Categories.jsx sends { categoryName, description } on create/update,
 * and reads { categoryID, categoryName, description } back.
 * Since Category has no relations, one DTO safely covers both directions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    // Ignored by the client on create, but returned on read.
    private Long categoryID;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be under 100 characters")
    private String categoryName;

    private String description;
}
