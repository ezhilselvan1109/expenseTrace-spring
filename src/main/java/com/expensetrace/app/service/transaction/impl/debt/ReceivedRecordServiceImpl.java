package com.expensetrace.app.service.transaction.impl.debt;

import com.expensetrace.app.dto.request.transaction.record.DebtReceivedRequestDto;
import com.expensetrace.app.dto.response.transaction.record.DebtReceivedResponseDto;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import com.expensetrace.app.repository.DebtRepository;
import com.expensetrace.app.account.repository.AccountRepository;
import com.expensetrace.app.account.repository.PaymentModeRepository;
import com.expensetrace.app.repository.transaction.debt.ReceivedRecordRepository;
import com.expensetrace.app.service.transaction.DebtTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("receivedRecordService")
@RequiredArgsConstructor
public class ReceivedRecordServiceImpl implements DebtTransactionService<DebtReceivedRequestDto, DebtReceivedResponseDto> {
    private final ReceivedRecordRepository repository;
    private final ModelMapper mapper;
    private final AccountRepository accountRepository;
    private final PaymentModeRepository paymentModeRepository;
    private final DebtRepository debtRepository;

    @Override
    public DebtReceivedResponseDto create(DebtReceivedRequestDto request, UUID debtId) {
        ReceivedRecord transaction = new ReceivedRecord();

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        }
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        transaction.setDebt(debt);
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setAccount(account);

        ReceivedRecord saved = repository.save(transaction);

        return mapper.map(saved, DebtReceivedResponseDto.class);
    }

    @Override
    public DebtReceivedResponseDto update(UUID id, DebtReceivedRequestDto request) {
        ReceivedRecord transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        transaction.setAccount(account);

        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        } else {
            transaction.setPaymentMode(null);
        }

        ReceivedRecord saved = repository.save(transaction);
        return mapper.map(saved, DebtReceivedResponseDto.class);
    }
}
