package com.expensetrace.app.repository.account;

import com.expensetrace.app.model.account.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}

