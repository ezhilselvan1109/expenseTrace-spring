package com.expensetrace.app.repository.account;

import com.expensetrace.app.model.account.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BankRepository extends JpaRepository<Bank, UUID> {
}

