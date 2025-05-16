package com.expensetrace.app.repository;

import com.expensetrace.app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface TagRepository extends JpaRepository<Tag, UUID> {
    Tag findByName(String name);

    boolean existsByName(String name);
    List<Tag> findByUserId(UUID userId);
    boolean existsByNameAndUserId(String name, UUID userId);
}

