package com.expensetrace.app.repository;

import com.expensetrace.app.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}

