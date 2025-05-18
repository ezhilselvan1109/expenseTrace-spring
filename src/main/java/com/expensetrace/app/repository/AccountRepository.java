package com.expensetrace.app.repository;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUserIdAndIsDefaultTrue(UUID userId);
    List<Account> findByUserId(UUID userId);
    List<Account> findByUserIdAndType(UUID userId,AccountType type);
}

