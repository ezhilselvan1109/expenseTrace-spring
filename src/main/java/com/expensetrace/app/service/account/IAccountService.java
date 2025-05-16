package com.expensetrace.app.service.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.responseDto.AccountResponseDto;

import java.util.List;
import java.util.Map;

public interface IAccountService {
    AccountResponseDto getAccountById(Long id);
    AccountResponseDto getAccountByName(String name);
    AccountResponseDto addAccount(AccountRequestDto accountRequest);
    AccountResponseDto updateAccount(AccountRequestDto accountRequest, Long id);
    AccountResponseDto getDefaultPaymentModeByUserId();
    AccountResponseDto updateDefaultPaymentMode(Long accountId);
    Map<AccountType, List<AccountResponseDto>> getAllAccountsByUserGroupedByType();
    void deleteAccountById(Long id);
}