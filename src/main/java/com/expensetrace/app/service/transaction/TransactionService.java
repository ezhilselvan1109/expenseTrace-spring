package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.request.transaction.*;
import com.expensetrace.app.dto.request.transaction.record.AdjustmentRequestDto;
import com.expensetrace.app.dto.request.transaction.record.PaidRequestDto;
import com.expensetrace.app.dto.request.transaction.record.ReceivedRequestDto;
import com.expensetrace.app.dto.response.transaction.*;
import com.expensetrace.app.dto.response.transaction.record.AdjustmentResponseDto;
import com.expensetrace.app.dto.response.transaction.record.PaidResponseDto;
import com.expensetrace.app.dto.response.transaction.record.ReceivedResponseDto;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.transaction.*;
import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import com.expensetrace.app.model.transaction.record.PaidRecord;
import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import com.expensetrace.app.repository.transaction.*;
import com.expensetrace.app.service.transaction.adjustment.IAdjustmentTransactionService;
import com.expensetrace.app.service.transaction.expense.IExpenseTransactionService;
import com.expensetrace.app.service.transaction.income.IIncomeTransactionService;
import com.expensetrace.app.service.transaction.record.adjustment.IAdjustmentService;
import com.expensetrace.app.service.transaction.record.paid.IPaidService;
import com.expensetrace.app.service.transaction.record.received.IReceivedService;
import com.expensetrace.app.service.transaction.transfer.ITransferTransactionService;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepo;

    private final IIncomeTransactionService incomeTransactionService;
    private final IExpenseTransactionService expenseTransactionService;
    private final ITransferTransactionService transferTransactionService;
    private final IAdjustmentTransactionService adjustmentTransactionService;
    private final IPaidService paidService;
    private final IReceivedService receivedService;
    private final IAdjustmentService adjustmentService;

    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    @Override
    public TransactionResponseDto createTransaction(TransactionRequestDto dto) {
        return switch (dto.getType()) {
            case EXPENSE -> {
                ExpenseTransaction txn=new ExpenseTransaction();
                populateCommon(txn, dto);
                yield expenseTransactionService.save(txn, (ExpenseTransactionRequestDto) dto);
            }
            case INCOME -> {
                IncomeTransaction txn = new IncomeTransaction();
                populateCommon(txn, dto);
                yield incomeTransactionService.save(txn, (IncomeTransactionRequestDto) dto);
            }
            case TRANSFER -> {
                TransferTransaction txn = new TransferTransaction();
                populateCommon(txn, dto);
                yield transferTransactionService.save(txn, (TransferTransactionRequestDto) dto);
            }
            case ADJUSTMENT -> {
                AdjustmentTransaction txn = new AdjustmentTransaction();
                populateCommon(txn, dto);
                yield adjustmentTransactionService.save(txn, (AdjustmentTransactionRequestDto) dto);
            }
            case PAID -> {
                PaidRecord txn = new PaidRecord();
                populateCommon(txn, dto);
                yield paidService.save(txn, (PaidRequestDto) dto);
            }
            case RECEIVED -> {
                ReceivedRecord txn = new ReceivedRecord();
                populateCommon(txn, dto);
                yield receivedService.save(txn, (ReceivedRequestDto) dto);
            }
            case DEBT_ADJUSTMENT -> {
                AdjustmentRecord txn = new AdjustmentRecord();
                populateCommon(txn, dto);
                yield adjustmentService.save(txn, (AdjustmentRequestDto) dto);
            }
        };
    }

    @Override
    public TransactionResponseDto createTransaction(UUID debtId, TransactionRequestDto dto) {
        return switch (dto.getType()) {
            case EXPENSE, INCOME, TRANSFER, ADJUSTMENT ->
                    throw new UnsupportedOperationException("Not supported yet: " + dto.getType());
            case PAID ->{
                PaidRecord txn = new PaidRecord();
                populateCommon(txn, dto);
                yield paidService.save(txn, (PaidRequestDto) dto);
            }
            case RECEIVED -> {
                ReceivedRecord txn = new ReceivedRecord();
                populateCommon(txn, dto);
                yield receivedService.save(txn, (ReceivedRequestDto) dto);
            }
            case DEBT_ADJUSTMENT -> {
                AdjustmentRecord txn = new AdjustmentRecord();
                populateCommon(txn, dto);
                yield adjustmentService.save(txn, (AdjustmentRequestDto) dto);
            }
        };
    }

    @Transactional
    public TransactionResponseDto updateTransaction(UUID id, TransactionRequestDto dto) {
        Transaction txn = loadExisting(id);
        populateCommon(txn, dto);
        return switch (dto.getType()) {
            case EXPENSE -> expenseTransactionService.save((ExpenseTransaction) txn, (ExpenseTransactionRequestDto) dto);
            case INCOME -> incomeTransactionService.save((IncomeTransaction)txn, (IncomeTransactionRequestDto) dto);
            case TRANSFER -> transferTransactionService.save((TransferTransaction) txn, (TransferTransactionRequestDto) dto);
            case ADJUSTMENT -> adjustmentTransactionService.save((AdjustmentTransaction)txn, (AdjustmentTransactionRequestDto) dto);
            case PAID -> paidService.save(txn, (PaidRequestDto) dto);
            case RECEIVED -> receivedService.save(txn, (ReceivedRequestDto) dto);
            case DEBT_ADJUSTMENT -> adjustmentService.save(txn, (AdjustmentRequestDto) dto);
        };
    }

    @Transactional
    public void deleteTransactionById(UUID id) {
        Transaction txn = transactionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepo.delete(txn);
    }

    @Override
    public TransactionResponseDto getTransactionById(UUID id) {

        Transaction txn = transactionRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        return switch (txn.getType()) {
            case EXPENSE -> modelMapper.map(txn, ExpenseTransactionResponseDto.class);
            case INCOME -> modelMapper.map(txn, IncomeTransactionResponseDto.class);
            case TRANSFER -> modelMapper.map(txn, TransferTransactionResponseDto.class);
            case ADJUSTMENT -> modelMapper.map(txn, AdjustmentTransactionResponseDto.class);
            case PAID -> modelMapper.map(txn, PaidResponseDto.class);
            case RECEIVED -> modelMapper.map(txn, ReceivedResponseDto.class);
            case DEBT_ADJUSTMENT -> modelMapper.map(txn, AdjustmentResponseDto.class);
        };
    }

    @Override
    public Page<TransactionResponseDto> getAllTransactions(int page, int size, TransactionType type) {
        return switch (type) {
            case EXPENSE -> expenseTransactionService.getAllExpenseTransactions(page, size);
            case INCOME -> incomeTransactionService.getAllIncomeTransactions(page, size);
            case TRANSFER -> transferTransactionService.getAllTransferTransactions(page, size);
            case ADJUSTMENT -> adjustmentTransactionService.getAllAdjustmentTransactions(page, size);
            case PAID, RECEIVED, DEBT_ADJUSTMENT ->
                    throw new UnsupportedOperationException("Not supported yet: " + type);
        };
    }

    @Override
    public Page<TransactionResponseDto> getAllTransactions(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepo.findAllByUserId(userId, pageable)
                .map(txn -> {
                    if (txn.getType().equals(TransactionType.EXPENSE)) {
                        return modelMapper.map(txn, ExpenseTransactionResponseDto.class);
                    } else if (txn.getType().equals(TransactionType.INCOME)) {
                        return modelMapper.map(txn, IncomeTransactionResponseDto.class);
                    } else if (txn.getType().equals(TransactionType.TRANSFER)) {
                        return modelMapper.map(txn, TransferTransactionResponseDto.class);
                    } else if (txn.getType().equals(TransactionType.ADJUSTMENT)) {
                        return modelMapper.map(txn, AdjustmentTransactionResponseDto.class);
                    } else {
                        return modelMapper.map(txn, TransactionResponseDto.class);
                    }
                });
    }

    public Page<TransactionResponseDto> getAllRecords(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepo.findAllRecordByDebtId(debtId, pageable)
                .map(txn -> {
                    if (txn.getType().equals(TransactionType.PAID)) {
                        return modelMapper.map(txn, PaidResponseDto.class);
                    } else if (txn.getType().equals(TransactionType.RECEIVED)) {
                        return modelMapper.map(txn, ReceivedResponseDto.class);
                    }
                    return null;
                });
    }

    // --- Helpers ---
    private void populateCommon(Transaction txn, TransactionRequestDto dto) {
        txn.setType(dto.getType());
        txn.setDate(dto.getDate());
        txn.setMonth(dto.getMonth());
        txn.setYear(dto.getYear());
        txn.setAmount(dto.getAmount());
        txn.setDescription(dto.getDescription());
    }

    private Transaction loadExisting(UUID id) {
        Transaction t = transactionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return t;
    }
}
