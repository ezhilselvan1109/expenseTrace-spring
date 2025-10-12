package com.expensetrace.app.account.service;

import com.expensetrace.app.account.dto.request.AccountRequestDto;
import com.expensetrace.app.account.dto.response.AccountResponseDto;
import com.expensetrace.app.account.enums.AccountType;
import com.expensetrace.app.account.service.impl.BankService;
import com.expensetrace.app.account.service.impl.CashService;
import com.expensetrace.app.account.service.impl.CreditCardService;
import com.expensetrace.app.account.service.impl.WalletService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    private final BankService bankService;
    private final WalletService walletService;
    private final CreditCardService creditCardService;
    private final CashService cashService;

    private Map<AccountType, AccountServices<?, ?>> serviceMap;

    @PostConstruct
    private void init() {
        serviceMap = Map.of(
                AccountType.BANK, bankService,
                AccountType.WALLET, walletService,
                AccountType.CREDIT_CARD, creditCardService,
                AccountType.CASH, cashService
        );
    }

    @SuppressWarnings("unchecked")
    public <T extends AccountRequestDto,
            R extends AccountResponseDto>
    AccountServices<T, R> getService(AccountType type) {
        return (AccountServices<T, R>) serviceMap.get(type);
    }
}


