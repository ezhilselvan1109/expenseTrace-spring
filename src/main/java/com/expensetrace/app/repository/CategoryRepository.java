package com.expensetrace.app.repository;

import com.expensetrace.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category findByName(String name);
    boolean existsByNameAndUserId(String name, UUID userId);
    List<Category> findByUserId(UUID userId);
    Optional<Category> findByIdAndUserId(UUID id, UUID userId);
}
