package com.expensetrace.app.service.account;

import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Account;
import com.expensetrace.app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account not found!"));
    }

    @Override
    public Account getAccountByName(String name) {
        return accountRepository.findByName(name);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account addAccount(AccountRequestDto accountRequest) {
        Account account = modelMapper.map(accountRequest, Account.class);
        System.out.println("category : "+account.toString());
        return  Optional.of(account)
                .map(accountRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(account.getName()+" already exists"));
    }

    @Override
    public Account updateAccount(AccountRequestDto account, Long id) {
        return Optional.ofNullable(getAccountById(id)).map(oldAccount -> {
            oldAccount.setName(account.getName());
            return accountRepository.save(oldAccount);
        }) .orElseThrow(()-> new ResourceNotFoundException("Account not found!"));
    }


    @Override
    public void deleteAccountById(Long id) {
        accountRepository.findById(id)
                .ifPresentOrElse(accountRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
