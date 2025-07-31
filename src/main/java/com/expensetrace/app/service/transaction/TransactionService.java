package com.expensetrace.app.service.transaction;

import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.*;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.repository.*;
import com.expensetrace.app.dto.request.TransactionRequestDto;
import com.expensetrace.app.dto.response.TransactionResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements  ITransactionService{

    private final TransactionRepository transactionRepo;
    private final CategoryRepository categoryRepo;
    private final AccountRepository accountRepo;
    private final TagRepository tagRepo;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public TransactionResponseDto createTransaction(TransactionRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User user=new User();
        user.setId(userId);
        Transaction txn = new Transaction();
        txn.setUser(user);
        txn.setType(dto.getType());
        txn.setDate(dto.getDate());
        txn.setMonth(dto.getMonth());
        txn.setYear(dto.getYear());
        txn.setAmount(dto.getAmount());
        txn.setDescription(dto.getDescription());

        // Set category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            txn.setCategory(category);
        }

        // Set account
        if (dto.getAccountId() != null) {
            Account account = accountRepo.findById(dto.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            txn.setAccount(account);
        }

        // Set tags
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepo.findAllById(dto.getTagIds());
            txn.setTags(new HashSet<>(tags));
        }

        Transaction savedTxn = transactionRepo.save(txn);
        return modelMapper.map(savedTxn, TransactionResponseDto.class);
    }

    public List<TransactionResponseDto> getAllTransactionsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return transactionRepo.findByUserId(userId).stream()
                .map(txn -> modelMapper.map(txn, TransactionResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionResponseDto> getAllTransactionsByUser(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Transaction> txnPage = transactionRepo.findByUserId(userId, pageable);

        return txnPage.map(txn -> modelMapper.map(txn, TransactionResponseDto.class));
    }


    public TransactionResponseDto getTransactionById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Transaction txn = transactionRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return modelMapper.map(txn, TransactionResponseDto.class);
    }

    public void deleteTransactionById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Transaction txn = transactionRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepo.delete(txn);
    }

    public TransactionResponseDto updateTransaction(UUID id, TransactionRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Transaction txn = transactionRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        txn.setType(dto.getType());
        txn.setDate(dto.getDate());
        txn.setMonth(dto.getMonth());
        txn.setYear(dto.getYear());
        txn.setAmount(dto.getAmount());
        txn.setDescription(dto.getDescription());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            txn.setCategory(category);
        }

        if (dto.getAccountId() != null) {
            Account account = accountRepo.findById(dto.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            txn.setAccount(account);
        }

        if (dto.getTagIds() != null) {
            List<Tag> tags = tagRepo.findAllById(dto.getTagIds());
            txn.setTags(new HashSet<>(tags));
        }

        Transaction updated = transactionRepo.save(txn);
        return modelMapper.map(updated, TransactionResponseDto.class);
    }
}
