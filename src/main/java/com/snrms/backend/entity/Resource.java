package com.snrms.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a row in the "resources" table.
 * Matches Resources.jsx: resourceName, categoryID (FK), location, quantity, unit, status.
 *
 * The frontend's dropdown sends a plain categoryID when creating/updating a
 * resource, but when *reading* a resource it expects a nested "category"
 * object (row.category?.categoryName). @ManyToOne gives us both for free:
 * Hibernate stores just the foreign key column, but when we fetch the
 * Resource, Jackson serializes the whole linked Category object too
 * (see ResourceDTO/ResourceMapper for how we control exactly what's exposed).
 */
@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceID;

    @Column(nullable = false)
    private String resourceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String location;

    private Double quantity;

    private String unit;

    // Available | Active | Protected | Depleted | Under Review
    @Column(nullable = false)
    private String status = "Available";
}
