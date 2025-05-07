package com.expensetrace.app.repository;

import com.expensetrace.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);

    boolean existsByName(String name);

    Optional<Account> findByUserIdAndIsDefaultTrue(Long userId);

    List<Account> findByUserId(Long userId);
}

