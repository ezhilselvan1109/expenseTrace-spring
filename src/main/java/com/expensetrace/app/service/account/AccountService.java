package com.expensetrace.app.service.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.*;
import com.expensetrace.app.repository.*;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.requestDto.BankAccountRequestDto;
import com.expensetrace.app.requestDto.CreditCardAccountRequestDto;
import com.expensetrace.app.requestDto.WalletAccountRequestDto;
import com.expensetrace.app.responseDto.*;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final BankAccountRepository bankAccountRepository;
    private final WalletAccountRepository walletAccountRepository;
    private final CreditCardAccountRepository creditCardAccountRepository;
    private final CashAccountRepository cashAccountRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Override
    public AccountResponseDto getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        if(account.getType()==AccountType.BANK){
            return modelMapper.map(account, BankAccountResponseDto.class);
        }else if(account.getType()==AccountType.WALLET){
            return modelMapper.map(account, WalletAccountResponseDto.class);
        }else if(account.getType()==AccountType.CREDIT_CARD){
            return modelMapper.map(account, CreditCardAccountResponseDto.class);
        }else {
            return modelMapper.map(account, CashAccountResponseDto.class);
        }
    }

    @Override
    public Map<AccountType, List<AccountResponseDto>> getAllAccountsByUserGroupedByType() {
        UUID userId = securityUtil.getAuthenticatedUserId();

        return accountRepository.findByUserId(userId)
                .stream()
                .map(account -> modelMapper.map(account, AccountResponseDto.class))
                .collect(Collectors.groupingBy(AccountResponseDto::getType));
    }

    @Override
    public List<AccountResponseDto> getAllBankAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.BANK)
                .stream()
                .filter(account -> account instanceof BankAccount)
                .map(account -> (AccountResponseDto) modelMapper.map((BankAccount) account, BankAccountResponseDto.class))
                .toList();
    }

    @Override
    public List<AccountResponseDto> getAllCashAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.CASH)
                .stream()
                .filter(account -> account instanceof CashAccount)
                .map(account -> (AccountResponseDto) modelMapper.map((CashAccount) account, CashAccountResponseDto.class))
                .toList();
    }

    @Override
    public List<AccountResponseDto> getAllCreditCardAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.CREDIT_CARD)
                .stream()
                .filter(account -> account instanceof CreditCardAccount)
                .map(account -> (AccountResponseDto) modelMapper.map((CreditCardAccount) account, CreditCardAccountResponseDto.class))
                .toList();
    }

    @Override
    public List<AccountResponseDto> getAllWalletAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.WALLET)
                .stream()
                .filter(account -> account instanceof WalletAccount)
                .map(account -> (AccountResponseDto) modelMapper.map((WalletAccount) account, WalletAccountResponseDto.class))
                .toList();
    }

    @Override
    public AccountResponseDto getDefaultPaymentModeByUserId() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Account defaultAccount = accountRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No default account found for user"));
        return modelMapper.map(defaultAccount, AccountResponseDto.class);
    }

    @Override
    public AccountResponseDto updateDefaultPaymentMode(UUID accountId) {
        UUID userId = securityUtil.getAuthenticatedUserId();
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

        accountToSetDefault.setDefault(true);
        accountRepository.save(accountToSetDefault);

        return modelMapper.map(accountToSetDefault, AccountResponseDto.class);
    }

    @Override
    public void deleteAccountById(UUID id) {
        accountRepository.findById(id).ifPresentOrElse(accountRepository::delete, () -> {
            throw new ResourceNotFoundException("Account not found!");
        });
    }

    @Override
    public AccountResponseDto addCreditCardAccount(CreditCardAccountRequestDto creditCardAccountRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        CreditCardAccount account = modelMapper.map(creditCardAccountRequestDto, CreditCardAccount.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.CREDIT_CARD);
        Account savedAccount = creditCardAccountRepository.save(account);
        return modelMapper.map(savedAccount, CreditCardAccountResponseDto.class);
    }

    @Override
    public void addCashAccount(User user) {
        CashAccount account=new CashAccount();
        account.setUser(user);
        account.setType(AccountType.CASH);
        account.setDefault(true);
        account.setCurrentBalance(BigDecimal.valueOf(0));
        account.setName("Cash");
        cashAccountRepository.save(account);
    }

    @Override
    public AccountResponseDto addWalletAccount(WalletAccountRequestDto walletAccountRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        WalletAccount account = modelMapper.map(walletAccountRequestDto, WalletAccount.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.WALLET);
        Account savedAccount = walletAccountRepository.save(account);
        return modelMapper.map(savedAccount, WalletAccountResponseDto.class);
    }

    @Override
    public AccountResponseDto addBankAccount(BankAccountRequestDto bankAccountRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        BankAccount account = modelMapper.map(bankAccountRequestDto, BankAccount.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.BANK);
        if (account.getType() == AccountType.BANK && bankAccountRequestDto.getLinkedPaymentModes() != null) {
            List<PaymentMode> paymentModes = bankAccountRequestDto.getLinkedPaymentModes().stream()
                    .map(pmDto -> {
                        PaymentMode pm = new PaymentMode();
                        pm.setName(pmDto.getName());
                        pm.setType(pmDto.getType());
                        pm.setAccount(account);
                        return pm;
                    })
                    .toList();

            account.setPaymentModes(paymentModes);
        }
        Account savedAccount = bankAccountRepository.save(account);
        return modelMapper.map(savedAccount, BankAccountResponseDto.class);
    }

    @Override
    public AccountResponseDto updateBankAccount(BankAccountRequestDto bankAccountDto, UUID id) {
        BankAccount existing = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(bankAccountDto.getName());
        existing.setCurrentBalance(bankAccountDto.getCurrentBalance());

        BankAccount updated = bankAccountRepository.save(existing);
        return modelMapper.map(updated, BankAccountResponseDto.class);
    }

    @Override
    public AccountResponseDto updateWalletAccount(WalletAccountRequestDto walletAccountRequestDto, UUID id) {
        WalletAccount existing = walletAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(walletAccountRequestDto.getName());
        existing.setCurrentBalance(walletAccountRequestDto.getCurrentBalance());
        Account updated = walletAccountRepository.save(existing);
        return modelMapper.map(updated, WalletAccountResponseDto.class);
    }

    @Override
    public AccountResponseDto updateCreditCardAccount(CreditCardAccountRequestDto accountRequestDto, UUID id) {
        CreditCardAccount existing = creditCardAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(accountRequestDto.getName());
        existing.setPaymentDueDate(accountRequestDto.getPaymentDueDate());
        existing.setCurrentAvailableLimit(accountRequestDto.getCurrentAvailableLimit());
        existing.setTotalCreditLimit(accountRequestDto.getTotalCreditLimit());
        existing.setBillingCycleStartDate(accountRequestDto.getBillingCycleStartDate());
        Account updated = creditCardAccountRepository.save(existing);
        return modelMapper.map(updated, CreditCardAccountResponseDto.class);
    }

    @Override
    public BigDecimal getAvailableAmount() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        BigDecimal bankTotal = bankAccountRepository.findAll()
                .stream()
                .filter(acc -> acc.getUser().getId().equals(userId))
                .map(BankAccount::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cashTotal = cashAccountRepository.findAll()
                .stream()
                .filter(acc -> acc.getUser().getId().equals(userId))
                .map(CashAccount::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal walletTotal = walletAccountRepository.findAll()
                .stream()
                .filter(acc -> acc.getUser().getId().equals(userId))
                .map(WalletAccount::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return bankTotal.add(cashTotal).add(walletTotal);
    }

    @Override
    public BigDecimal getCreditOutstanding() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        List<CreditCardAccount> accounts = creditCardAccountRepository.findAll(); // You can optimize with a custom query later

        return accounts.stream()
                .filter(account -> account.getUser().getId().equals(userId))
                .map(account -> account.getTotalCreditLimit().subtract(account.getCurrentAvailableLimit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getCreditAvailable() {
        UUID userId = securityUtil.getAuthenticatedUserId();

        return creditCardAccountRepository.findByUserId(userId).stream()
                .map(CreditCardAccount::getCurrentAvailableLimit)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
