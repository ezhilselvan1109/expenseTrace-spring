package com.expensetrace.app.service.account;

import com.expensetrace.app.dto.response.account.*;
import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.*;
import com.expensetrace.app.model.account.*;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.dto.request.account.BankRequestDto;
import com.expensetrace.app.dto.request.account.CreditCardRequestDto;
import com.expensetrace.app.dto.request.account.WalletRequestDto;
import com.expensetrace.app.repository.account.*;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final WalletRepository walletRepository;
    private final CreditCardRepository creditCardRepository;
    private final CashRepository cashRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Override
    public AccountResponseDto getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        if(account.getType()==AccountType.BANK){
            return modelMapper.map(account, BankResponseDto.class);
        }else if(account.getType()==AccountType.WALLET){
            return modelMapper.map(account, WalletResponseDto.class);
        }else if(account.getType()==AccountType.CREDIT_CARD){
            return modelMapper.map(account, CreditCardResponseDto.class);
        }else {
            return modelMapper.map(account, CashResponseDto.class);
        }
    }

    @Override
    public List<AccountResponseDto> getAllAccountsByUserGroupedByType() {
        UUID userId = securityUtil.getAuthenticatedUserId();

        return accountRepository.findByUserId(userId)
                .stream()
                .map(account -> modelMapper.map(account, AccountResponseDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<AccountResponseDto> getAllBankAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.BANK)
                .stream()
                .filter(account -> account instanceof Bank)
                .map(account -> (AccountResponseDto) modelMapper.map((Bank) account, BankResponseDto.class))
                .toList();
    }

    @Override
    public List<AccountResponseDto> getAllCashAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.CASH)
                .stream()
                .filter(account -> account instanceof Cash)
                .map(account -> (AccountResponseDto) modelMapper.map((Cash) account, CashResponseDto.class))
                .toList();
    }

    @Override
    public List<AccountResponseDto> getAllCreditCardAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.CREDIT_CARD)
                .stream()
                .filter(account -> account instanceof CreditCard)
                .map(account -> (AccountResponseDto) modelMapper.map((CreditCard) account, CreditCardResponseDto.class))
                .toList();
    }

    @Override
    public List<AccountResponseDto> getAllWalletAccountsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return accountRepository.findByUserIdAndType(userId,AccountType.WALLET)
                .stream()
                .filter(account -> account instanceof Wallet)
                .map(account -> (AccountResponseDto) modelMapper.map((Wallet) account, WalletResponseDto.class))
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
    public AccountResponseDto addCreditCardAccount(CreditCardRequestDto creditCardRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        CreditCard account = modelMapper.map(creditCardRequestDto, CreditCard.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.CREDIT_CARD);
        Account savedAccount = creditCardRepository.save(account);
        return modelMapper.map(savedAccount, CreditCardResponseDto.class);
    }

    @Override
    public void addCashAccount(User user) {
        Cash account=new Cash();
        account.setUser(user);
        account.setType(AccountType.CASH);
        account.setDefault(true);
        account.setCurrentBalance(BigDecimal.valueOf(0));
        account.setName("Cash");
        cashRepository.save(account);
    }

    @Override
    public AccountResponseDto addWalletAccount(WalletRequestDto walletRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Wallet account = modelMapper.map(walletRequestDto, Wallet.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.WALLET);
        Account savedAccount = walletRepository.save(account);
        return modelMapper.map(savedAccount, WalletResponseDto.class);
    }

    @Override
    public AccountResponseDto addBankAccount(BankRequestDto bankRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Bank account = modelMapper.map(bankRequestDto, Bank.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.BANK);
        if (account.getType() == AccountType.BANK && bankRequestDto.getLinkedPaymentModes() != null) {
            List<PaymentMode> paymentModes = bankRequestDto.getLinkedPaymentModes().stream()
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
        Account savedAccount = bankRepository.save(account);
        return modelMapper.map(savedAccount, BankResponseDto.class);
    }

    @Override
    public AccountResponseDto updateBankAccount(BankRequestDto bankAccountDto, UUID id) {
        Bank existing = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(bankAccountDto.getName());
        existing.setCurrentBalance(bankAccountDto.getCurrentBalance());

        Bank updated = bankRepository.save(existing);
        return modelMapper.map(updated, BankResponseDto.class);
    }

    @Override
    public AccountResponseDto updateWalletAccount(WalletRequestDto walletRequestDto, UUID id) {
        Wallet existing = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(walletRequestDto.getName());
        existing.setCurrentBalance(walletRequestDto.getCurrentBalance());
        Account updated = walletRepository.save(existing);
        return modelMapper.map(updated, WalletResponseDto.class);
    }

    @Override
    public AccountResponseDto updateCreditCardAccount(CreditCardRequestDto accountRequestDto, UUID id) {
        CreditCard existing = creditCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(accountRequestDto.getName());
        existing.setPaymentDueDate(accountRequestDto.getPaymentDueDate());
        existing.setCurrentAvailableLimit(accountRequestDto.getCurrentAvailableLimit());
        existing.setTotalCreditLimit(accountRequestDto.getTotalCreditLimit());
        existing.setBillingCycleStartDate(accountRequestDto.getBillingCycleStartDate());
        Account updated = creditCardRepository.save(existing);
        return modelMapper.map(updated, CreditCardResponseDto.class);
    }

    @Override
    public BigDecimal getAvailableAmount() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        BigDecimal bankTotal = bankRepository.findAll()
                .stream()
                .filter(acc -> acc.getUser().getId().equals(userId))
                .map(Bank::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cashTotal = cashRepository.findAll()
                .stream()
                .filter(acc -> acc.getUser().getId().equals(userId))
                .map(Cash::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal walletTotal = walletRepository.findAll()
                .stream()
                .filter(acc -> acc.getUser().getId().equals(userId))
                .map(Wallet::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return bankTotal.add(cashTotal).add(walletTotal);
    }

    @Override
    public BigDecimal getCreditOutstanding() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        List<CreditCard> accounts = creditCardRepository.findAll(); // You can optimize with a custom query later

        return accounts.stream()
                .filter(account -> account.getUser().getId().equals(userId))
                .map(account -> account.getTotalCreditLimit().subtract(account.getCurrentAvailableLimit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getCreditAvailable() {
        UUID userId = securityUtil.getAuthenticatedUserId();

        return creditCardRepository.findByUserId(userId).stream()
                .map(CreditCard::getCurrentAvailableLimit)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
