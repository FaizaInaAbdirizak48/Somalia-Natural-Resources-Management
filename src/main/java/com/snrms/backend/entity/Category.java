package com.snrms.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a row in the "categories" table.
 * Matches Categories.jsx exactly: categoryID, categoryName, description.
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @Column(columnDefinition = "TEXT")
    private String description;
}
