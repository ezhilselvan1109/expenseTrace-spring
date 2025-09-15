package com.expensetrace.app.repository;

import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    Page<Tag> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Tag> findByUserIdAndName(UUID userId, String name);
}

