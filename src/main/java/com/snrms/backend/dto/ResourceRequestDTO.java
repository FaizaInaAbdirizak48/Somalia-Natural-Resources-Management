package com.snrms.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Matches Resources.jsx's formData / payload exactly:
 *   { resourceName, categoryID, location, quantity, unit, status }
 */
@Data
public class ResourceRequestDTO {

    @NotBlank(message = "Resource name is required")
    private String resourceName;

    @NotNull(message = "Category is required")
    private Long categoryID;

    private String location;

    private Double quantity;

    private String unit;

    @Pattern(regexp = "Available|Active|Protected|Depleted|Under Review",
             message = "Status must be one of: Available, Active, Protected, Depleted, Under Review")
    private String status = "Available";
}
