package com.expensetrace.app.repository;

import com.expensetrace.app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    boolean existsByName(String name);
}

