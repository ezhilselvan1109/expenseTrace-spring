package com.expensetrace.app.repository;

import com.expensetrace.app.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
;
import java.util.UUID;


public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
}

