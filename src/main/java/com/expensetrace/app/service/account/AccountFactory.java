package com.expensetrace.app.service.account;

import com.expensetrace.app.dto.request.account.AccountRequestDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.service.account.impl.BankService;
import com.expensetrace.app.service.account.impl.CashService;
import com.expensetrace.app.service.account.impl.CreditCardService;
import com.expensetrace.app.service.account.impl.WalletService;
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


