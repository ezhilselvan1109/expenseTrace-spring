package com.expensetrace.app.service.account;

import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.model.Account;

import java.util.List;

public interface IAccountService {
    Account getAccountById(Long id);
    Account getAccountByName(String name);
    List<Account> getAllAccounts();
    Account addAccount(AccountRequestDto accountRequest);
    Account updateAccount(AccountRequestDto accountRequest, Long id);
    void deleteAccountById(Long id);
}
