package com.expensetrace.app.repository;

import com.expensetrace.app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    boolean existsByName(String name);
    List<Tag> findByUserId(Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
}

