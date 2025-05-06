package com.expensetrace.app.repository;

import com.expensetrace.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);

    boolean existsByName(String name);
}

