package com.snrms.backend.entity;

/**
 * The original spec asked for a separate "Role" entity/table.
 * But Users.jsx only ever sends/receives role as a plain string
 * ("Admin" or "Manager") - there's no role management UI, no permissions
 * editor, nothing that needs a roles table with its own ID.
 *
 * Building a full Role entity + join table here would add complexity the
 * frontend can't use yet, so we use this enum purely as a validation
 * reference (see RegisterDTO / UserRequestDTO @Pattern checks).
 * The actual column in the "users" table is just a VARCHAR.
 *
 * If your project later needs custom roles or per-role permissions,
 * this is the natural place to grow into a real @Entity.
 */
public enum Role {
    ADMIN("Admin"),
    MANAGER("Manager");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
