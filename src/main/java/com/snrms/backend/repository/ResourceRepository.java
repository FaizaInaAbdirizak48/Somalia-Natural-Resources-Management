package com.snrms.backend.repository;

import com.snrms.backend.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // Powers GET /api/Resources/search?name=... used by Resources.jsx and
    // the "searchResources" service function.
    List<Resource> findByResourceNameContainingIgnoreCase(String name);

    boolean existsByCategory_CategoryID(Long categoryId);
}
