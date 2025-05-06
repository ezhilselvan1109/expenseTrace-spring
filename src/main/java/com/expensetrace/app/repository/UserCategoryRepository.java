package com.expensetrace.app.repository;

import com.expensetrace.app.enums.CategoryType;
import com.expensetrace.app.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    List<UserCategory> findByUserIdAndCategory_Type(Long userId, CategoryType type);
    Optional<UserCategory> findByUserIdAndCategory_TypeAndIsDefaultTrue(Long userId, CategoryType type);
}
