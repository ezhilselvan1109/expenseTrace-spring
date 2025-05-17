package com.expensetrace.app.repository;

import com.expensetrace.app.model.WalletAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface WalletAccountRepository extends JpaRepository<WalletAccount, UUID> {
}

