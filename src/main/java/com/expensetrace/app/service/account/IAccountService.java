package com.expensetrace.app.service.account;

import com.expensetrace.app.model.User;
import com.expensetrace.app.dto.request.account.BankRequestDto;
import com.expensetrace.app.dto.request.account.CreditCardRequestDto;
import com.expensetrace.app.dto.request.account.WalletRequestDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IAccountService {
    AccountResponseDto getAccountById(UUID id);

    List<AccountResponseDto> getAllBankAccountsByUser();

    List<AccountResponseDto> getAllCashAccountsByUser();

    List<AccountResponseDto> getAllCreditCardAccountsByUser();

    List<AccountResponseDto> getAllWalletAccountsByUser();

    AccountResponseDto getDefaultPaymentModeByUserId();

    AccountResponseDto updateDefaultPaymentMode(UUID accountId);

    List<AccountResponseDto> getAllAccountsByUserGroupedByType();

    void deleteAccountById(UUID id);

    AccountResponseDto addCreditCardAccount(CreditCardRequestDto requestDto);

    AccountResponseDto addWalletAccount(WalletRequestDto walletRequestDto);

    AccountResponseDto addBankAccount(BankRequestDto requestDto);

    void addCashAccount(User user);

    AccountResponseDto updateBankAccount(BankRequestDto accountDto, UUID id);

    AccountResponseDto updateWalletAccount(WalletRequestDto accountDto, UUID id);

    AccountResponseDto updateCreditCardAccount(CreditCardRequestDto accountDto, UUID id);

    BigDecimal getAvailableAmount();

    BigDecimal getCreditOutstanding();

    BigDecimal getCreditAvailable();
}