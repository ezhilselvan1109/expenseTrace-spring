package com.expensetrace.app.repository;

import com.expensetrace.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByName(String name);

    boolean existsByName(String name);

    Optional<Account> findByUserIdAndIsDefaultTrue(UUID userId);

    List<Account> findByUserId(UUID userId);
}

