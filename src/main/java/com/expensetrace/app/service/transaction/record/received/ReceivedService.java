package com.expensetrace.app.service.transaction.record.received;

import com.expensetrace.app.dto.request.transaction.record.ReceivedRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.dto.response.transaction.record.ReceivedResponseDto;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import com.expensetrace.app.repository.transaction.record.ReceivedRecordRepository;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.service.paymentMode.IPaymentModeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ReceivedService implements IReceivedService {

    private final ReceivedRecordRepository receivedRecordRepo;

    private final IAccountService accountService;
    private final IPaymentModeService paymentModeService;

    private final ModelMapper modelMapper;

    public TransactionResponseDto save(Transaction transaction, ReceivedRequestDto dto) {
        ReceivedRecord txn = (ReceivedRecord) transaction;
        txn.setDebt(new Debt() {{
            setId(dto.getDebtId());
        }});
        setAccount(txn, dto.getAccountId());
        setPaymentMode(txn, dto.getPaymentModeId());
        Transaction saved = receivedRecordRepo.save(txn);
        return modelMapper.map(saved, ReceivedResponseDto.class);
    }

    @Override
    public Page<TransactionResponseDto> getAllReceivedRecords(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Transaction> debtPage = receivedRecordRepo.findByDebtIdAndType(debtId, TransactionType.RECEIVED, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, ReceivedResponseDto.class));
    }

    @Override
    public BigDecimal getTotalReceived(UUID debtId) {
        return receivedRecordRepo.findTotalAmountByDebtIdAndType(debtId, TransactionType.RECEIVED);
    }

    public void setAccount(ReceivedRecord txn, UUID id) {
        if(id==null)return;
        Account account=accountService.getAccount(id);
        txn.setAccount(account);
    }

    public void setPaymentMode(ReceivedRecord txn, UUID id) {
        if(id==null)return;
        PaymentMode paymentMode = paymentModeService.getPaymentMode(id);
        txn.setPaymentMode(paymentMode);
    }
}
