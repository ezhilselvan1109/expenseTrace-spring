package com.expensetrace.app.service.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.User;
import com.expensetrace.app.requestDto.BankAccountRequestDto;
import com.expensetrace.app.requestDto.CreditCardAccountRequestDto;
import com.expensetrace.app.requestDto.WalletAccountRequestDto;
import com.expensetrace.app.responseDto.AccountResponseDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IAccountService {
    AccountResponseDto getAccountById(UUID id);

    List<AccountResponseDto> getAllBankAccountsByUser();

    List<AccountResponseDto> getAllCashAccountsByUser();

    List<AccountResponseDto> getAllCreditCardAccountsByUser();

    List<AccountResponseDto> getAllWalletAccountsByUser();

    AccountResponseDto getDefaultPaymentModeByUserId();

    AccountResponseDto updateDefaultPaymentMode(UUID accountId);

    Map<AccountType, List<AccountResponseDto>> getAllAccountsByUserGroupedByType();

    void deleteAccountById(UUID id);

    AccountResponseDto addCreditCardAccount(CreditCardAccountRequestDto requestDto);

    AccountResponseDto addWalletAccount(WalletAccountRequestDto walletAccountRequestDto);

    AccountResponseDto addBankAccount(BankAccountRequestDto requestDto);

    void addCashAccount(User user);

    AccountResponseDto updateBankAccount(BankAccountRequestDto accountDto, UUID id);

    AccountResponseDto updateWalletAccount(WalletAccountRequestDto accountDto, UUID id);

    AccountResponseDto updateCreditCardAccount(CreditCardAccountRequestDto accountDto, UUID id);


}