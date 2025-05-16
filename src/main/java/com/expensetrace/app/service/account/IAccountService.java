package com.expensetrace.app.service.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.responseDto.AccountResponseDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IAccountService {
    AccountResponseDto getAccountById(UUID id);
    AccountResponseDto getAccountByName(String name);
    AccountResponseDto addAccount(AccountRequestDto accountRequest);
    AccountResponseDto updateAccount(AccountRequestDto accountRequest, UUID id);
    AccountResponseDto getDefaultPaymentModeByUserId();
    AccountResponseDto updateDefaultPaymentMode(UUID accountId);
    Map<AccountType, List<AccountResponseDto>> getAllAccountsByUserGroupedByType();
    void deleteAccountById(UUID id);
}