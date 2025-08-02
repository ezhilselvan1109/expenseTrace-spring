package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.request.transaction.*;
import com.expensetrace.app.dto.response.transaction.*;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.*;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.transaction.*;
import com.expensetrace.app.repository.*;
import com.expensetrace.app.repository.account.AccountRepository;
import com.expensetrace.app.repository.transaction.*;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepo;
    private final ExpenseTransactionRepository expenseTransactionRepo;
    private final IncomeTransactionRepository incomeTransactionRepo;
    private final TransferTransactionRepository transferTransactionRepo;
    private final AdjustmentTransactionRepository adjustmentTransactionRepo;
    private final AccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final TagRepository tagRepo;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    @Override
    public TransactionResponseDto createTransaction(TransactionRequestDto dto) {
        System.out.println(dto);
        return switch (dto.getType()) {
            case EXPENSE -> createExpense((ExpenseTransactionRequestDto) dto);
            case INCOME -> createIncome((IncomeTransactionRequestDto) dto);
            case TRANSFER -> createTransfer((TransferTransactionRequestDto) dto);
            case ADJUSTMENT -> createAdjustment((AdjustmentTransactionRequestDto) dto);
        };
    }

    @Transactional
    public void deleteTransactionById(UUID id) {
        Transaction txn = transactionRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(securityUtil.getAuthenticatedUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepo.delete(txn);
    }

    @Transactional
    public TransactionResponseDto updateTransaction(UUID id, TransactionRequestDto dto) {
        return switch (dto.getType()) {
            case EXPENSE -> updateExpense(id, (ExpenseTransactionRequestDto) dto);
            case INCOME -> updateIncome(id, (IncomeTransactionRequestDto) dto);
            case TRANSFER -> updateTransfer(id, (TransferTransactionRequestDto) dto);
            case ADJUSTMENT -> updateAdjustment(id, (AdjustmentTransactionRequestDto) dto);
        };
    }

    // --- Creation methods ---
    private TransactionResponseDto createExpense(ExpenseTransactionRequestDto dto) {
        ExpenseTransaction txn = new ExpenseTransaction();
        populateCommon(txn, dto);
        setCategory(txn, dto.getCategoryId());
        setAccount(txn, dto.getAccountId());
        txn.setTags(resolveTags(dto.getTagIds(), dto.getTags()));
        Transaction saved = transactionRepo.save(txn);
        return modelMapper.map(saved, ExpenseTransactionResponseDto.class);
    }

    private TransactionResponseDto createIncome(IncomeTransactionRequestDto dto) {
        IncomeTransaction txn = new IncomeTransaction();
        populateCommon(txn, dto);
        setCategory(txn, dto.getCategoryId());
        setAccount(txn, dto.getAccountId());
        txn.setTags(resolveTags(dto.getTagIds(), dto.getTags()));
        Transaction saved = transactionRepo.save(txn);
        return modelMapper.map(saved, IncomeTransactionResponseDto.class);
    }

    private TransactionResponseDto createTransfer(TransferTransactionRequestDto dto) {
        TransferTransaction txn = new TransferTransaction();
        populateCommon(txn, dto);
        Account from = accountRepo.findById(dto.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));
        Account to = accountRepo.findById(dto.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));
        txn.setFromAccount(from);
        txn.setToAccount(to);
        txn.setTags(resolveTags(dto.getTagIds(), dto.getTags()));
        TransferTransaction saved = transferTransactionRepo.save(txn);
        return modelMapper.map(saved, TransferTransactionResponseDto.class);
    }

    private TransactionResponseDto createAdjustment(AdjustmentTransactionRequestDto dto) {
        AdjustmentTransaction txn = new AdjustmentTransaction();
        populateCommon(txn, dto);
        setAccount(txn, dto.getAccountId());
        Transaction saved = transactionRepo.save(txn);
        return modelMapper.map(saved, AdjustmentTransactionResponseDto.class);
    }

    // --- Update methods (similar but using findById & mapping fields) ---
    private TransactionResponseDto updateExpense(UUID id, ExpenseTransactionRequestDto dto) {
        ExpenseTransaction txn = (ExpenseTransaction) loadExisting(id, ExpenseTransaction.class);
        populateCommon(txn, dto);
        setCategory(txn, dto.getCategoryId());
        setAccount(txn, dto.getAccountId());
        txn.setTags(resolveTags(dto.getTagIds(), dto.getTags()));
        Transaction saved = transactionRepo.save(txn);
        return modelMapper.map(saved, ExpenseTransactionResponseDto.class);
    }

    private TransactionResponseDto updateIncome(UUID id, IncomeTransactionRequestDto dto) {
        IncomeTransaction txn = (IncomeTransaction) loadExisting(id, IncomeTransaction.class);
        populateCommon(txn, dto);
        setCategory(txn, dto.getCategoryId());
        setAccount(txn, dto.getAccountId());
        txn.setTags(resolveTags(dto.getTagIds(), dto.getTags()));
        Transaction saved = transactionRepo.save(txn);
        return modelMapper.map(saved, IncomeTransactionResponseDto.class);
    }

    private TransactionResponseDto updateTransfer(UUID id, TransferTransactionRequestDto dto) {
        TransferTransaction txn = (TransferTransaction) loadExisting(id, TransferTransaction.class);
        populateCommon(txn, dto);
        Account from = accountRepo.findById(dto.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));
        Account to = accountRepo.findById(dto.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));
        txn.setFromAccount(from);
        txn.setToAccount(to);
        txn.setTags(resolveTags(dto.getTagIds(), dto.getTags()));
        Transaction saved = transactionRepo.save(txn);
        return modelMapper.map(saved, TransferTransactionResponseDto.class);
    }

    private TransactionResponseDto updateAdjustment(UUID id, AdjustmentTransactionRequestDto dto) {
        AdjustmentTransaction txn = (AdjustmentTransaction) loadExisting(id, AdjustmentTransaction.class);
        populateCommon(txn, dto);
        setAccount(txn, dto.getAccountId());
        Transaction saved = transactionRepo.save(txn);
        return modelMapper.map(saved, AdjustmentTransactionResponseDto.class);
    }

    // --- Helpers ---
    private void populateCommon(Transaction txn, TransactionRequestDto dto) {
        txn.setUser(new User() {{ setId(securityUtil.getAuthenticatedUserId()); }});
        txn.setType(dto.getType());
        txn.setDate(dto.getDate());
        txn.setMonth(dto.getMonth());
        txn.setYear(dto.getYear());
        txn.setAmount(dto.getAmount());
        txn.setDescription(dto.getDescription());
    }

    private Transaction loadExisting(UUID id, Class<? extends Transaction> cls) {
        Transaction t = transactionRepo.findById(id)
                .filter(tx -> tx.getUser().getId().equals(securityUtil.getAuthenticatedUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (!cls.isInstance(t)) {
            throw new IllegalArgumentException("Transaction type mismatch");
        }
        return t;
    }

    private void setCategory(Transaction txn, UUID catId) {
        if (catId == null) return;

        Category category = categoryRepo.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (txn instanceof ExpenseTransaction expenseTxn) {
            expenseTxn.setCategory(category);
        } else if (txn instanceof IncomeTransaction incomeTxn) {
            incomeTxn.setCategory(category);
        } else {
            throw new IllegalArgumentException("Category not applicable for this transaction type");
        }
    }

    private void setAccount(Transaction txn, UUID acctId) {
        if (acctId == null) return;

        Account account = accountRepo.findById(acctId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (txn instanceof ExpenseTransaction expenseTxn) {
            expenseTxn.setAccount(account);
        } else if (txn instanceof IncomeTransaction incomeTxn) {
            incomeTxn.setAccount(account);
        } else if (txn instanceof AdjustmentTransaction adjustmentTxn) {
            adjustmentTxn.setAccount(account);
        } else {
            throw new IllegalArgumentException("Account not applicable for this transaction type");
        }
    }



    private Set<Tag> resolveTags(List<UUID> tagIds, List<String> names) {
        UUID uid = securityUtil.getAuthenticatedUserId();
        Set<Tag> tags = new HashSet<>();
        if (tagIds != null) {
            tags.addAll(tagRepo.findAllById(tagIds));
        }
        if (names != null) {
            for (String nm : names) {
                Tag tg = tagRepo.findByNameAndUserId(nm.trim(), uid)
                        .orElseGet(() -> {
                            Tag newT = new Tag();
                            newT.setName(nm.trim());
                            newT.setUser(new User() {{ setId(uid); }});
                            return tagRepo.save(newT);
                        });
                tags.add(tg);
            }
        }
        return tags;
    }


    @Override
    public TransactionResponseDto getTransactionByIdAndType(UUID id, TransactionType type) {
        return switch (type) {
            case EXPENSE -> {
                ExpenseTransaction txn = expenseTransactionRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Expense transaction not found"));
                yield modelMapper.map(txn, ExpenseTransactionResponseDto.class);
            }
            case INCOME -> {
                IncomeTransaction txn = incomeTransactionRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Income transaction not found"));
                yield modelMapper.map(txn, IncomeTransactionResponseDto.class);
            }
            case TRANSFER -> {
                TransferTransaction txn = transferTransactionRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Transfer transaction not found"));
                yield modelMapper.map(txn, TransferTransactionResponseDto.class);
            }
            case ADJUSTMENT -> {
                AdjustmentTransaction txn = adjustmentTransactionRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Adjustment transaction not found"));
                yield modelMapper.map(txn, AdjustmentTransactionResponseDto.class);
            }
        };
    }

}
