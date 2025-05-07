package com.expensetrace.app.service.account;

import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.responseDto.AccountResponseDto;

import java.util.List;

public interface IAccountService {
    AccountResponseDto getAccountById(Long id);
    AccountResponseDto getAccountByName(String name);
    List<AccountResponseDto> getAllAccounts();
    AccountResponseDto addAccount(AccountRequestDto accountRequest, Long userId);
    AccountResponseDto updateAccount(AccountRequestDto accountRequest, Long id);
    AccountResponseDto getDefaultPaymentModeByUserId(Long userId);
    AccountResponseDto updateDefaultPaymentMode(Long accountId, Long userId);

    void deleteAccountById(Long id);
}