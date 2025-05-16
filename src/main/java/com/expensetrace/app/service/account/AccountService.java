package com.expensetrace.app.service.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.Account;
import com.expensetrace.app.model.PaymentMode;
import com.expensetrace.app.model.User;
import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.repository.AccountRepository;
import com.expensetrace.app.responseDto.AccountResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;
    @Override
    public AccountResponseDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        return modelMapper.map(account, AccountResponseDto.class);
    }

    @Override
    public AccountResponseDto getDefaultPaymentModeByUserId() {
        Long userId = securityUtil.getAuthenticatedUserId();
        Account defaultAccount = accountRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No default account found for user"));
        return modelMapper.map(defaultAccount, AccountResponseDto.class);
    }

    @Override
    public AccountResponseDto updateDefaultPaymentMode(Long accountId) {
        Long userId = securityUtil.getAuthenticatedUserId();
        Account accountToSetDefault = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));

        if (!accountToSetDefault.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Account does not belong to the user");
        }

        List<Account> userAccounts = accountRepository.findByUserId(userId);
        userAccounts.forEach(a -> {
            if (a.isDefault()) {
                a.setDefault(false);
                accountRepository.save(a);
            }
        });

        // Set new default
        accountToSetDefault.setDefault(true);
        accountRepository.save(accountToSetDefault);

        return modelMapper.map(accountToSetDefault, AccountResponseDto.class);
    }

    @Override
    public AccountResponseDto getAccountByName(String name) {
        Account account = accountRepository.findByName(name);
        if (account == null) {
            throw new ResourceNotFoundException("Account not found with name: " + name);
        }
        return modelMapper.map(account, AccountResponseDto.class);
    }

    @Override
    public Map<AccountType, List<AccountResponseDto>> getAllAccountsByUserGroupedByType() {
        Long userId = securityUtil.getAuthenticatedUserId();

        return accountRepository.findByUserId(userId)
                .stream()
                .map(account -> modelMapper.map(account, AccountResponseDto.class))
                .collect(Collectors.groupingBy(AccountResponseDto::getType));
    }

    @Override
    public AccountResponseDto addAccount(AccountRequestDto accountRequestDto) {
        Long userId = securityUtil.getAuthenticatedUserId();
        if (accountRepository.existsByName(accountRequestDto.getName())) {
            throw new AlreadyExistsException(accountRequestDto.getName() + " already exists");
        }

        Account account = modelMapper.map(accountRequestDto, Account.class);

        User user = new User();
        user.setId(userId);
        account.setUser(user);

        if (account.getType() == AccountType.BANK) {
            PaymentMode paymentMode = new PaymentMode();
            paymentMode.setName(accountRequestDto.getPaymentModesDto().getName());
            paymentMode.setType(accountRequestDto.getPaymentModesDto().getType());
            paymentMode.setAccount(account);

            account.setPaymentModes(List.of(paymentMode));
        }

        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountResponseDto.class);
    }


    @Override
    public AccountResponseDto updateAccount(AccountRequestDto accountRequestDto, Long id) {
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));

        existing.setName(accountRequestDto.getName());
        existing.setType(accountRequestDto.getType());
        existing.setCurrentBalance(accountRequestDto.getCurrentBalance());
        existing.setAvailableCredit(accountRequestDto.getAvailableCredit());
        existing.setCreditLimit(accountRequestDto.getCreditLimit());
        existing.setBillingStart(accountRequestDto.getBillingStart());
        existing.setDueDate(accountRequestDto.getDueDate());

        Account updated = accountRepository.save(existing);
        return modelMapper.map(updated, AccountResponseDto.class);
    }


    @Override
    public void deleteAccountById(Long id) {
        accountRepository.findById(id)
                .ifPresentOrElse(accountRepository::delete,
                        () -> { throw new ResourceNotFoundException("Account not found!"); });
    }
}
