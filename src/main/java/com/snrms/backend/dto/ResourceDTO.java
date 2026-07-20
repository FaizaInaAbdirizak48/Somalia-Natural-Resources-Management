package com.snrms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What we send BACK for a resource. Resources.jsx reads:
 *   row.category?.categoryName  -> needs the nested "category" object
 * so this DTO carries both the flat categoryID (handy for re-opening the
 * edit form) AND the full nested CategoryDTO (for display).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {
    private Long resourceID;
    private String resourceName;
    private Long categoryID;
    private CategoryDTO category;
    private String location;
    private Double quantity;
    private String unit;
    private String status;
}
