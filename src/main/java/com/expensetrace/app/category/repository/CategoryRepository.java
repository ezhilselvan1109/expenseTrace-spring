package com.expensetrace.app.category.repository;

import com.expensetrace.app.category.enums.CategoryType;
import com.expensetrace.app.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByNameAndUserId(String name, UUID userId);
    List<Category> findByUserId(UUID userId);
    List<Category> findByUserIdAndType(UUID userId, CategoryType type);
    Optional<Category> findByIdAndUserId(UUID id, UUID userId);
    Optional<Category> findByUserIdAndTypeAndIsDefaultTrue(UUID userId,CategoryType type);
}
