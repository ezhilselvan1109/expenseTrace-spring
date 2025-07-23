package com.expensetrace.app.repository;

import com.expensetrace.app.model.Settings;
import com.expensetrace.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SettingRepository extends JpaRepository<Settings, UUID> {
    Optional<Settings> findByUser(User user);
}
