package com.expensetrace.app.service.tag;

import com.expensetrace.app.dto.request.TagRequestDto;
import com.expensetrace.app.dto.response.tag.TagsResponseDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.model.transaction.IncomeTransaction;
import com.expensetrace.app.model.transaction.TransferTransaction;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.repository.transaction.ExpenseTransactionRepository;
import com.expensetrace.app.repository.transaction.IncomeTransactionRepository;
import com.expensetrace.app.repository.transaction.TransferTransactionRepository;
import com.expensetrace.app.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    private final ExpenseTransactionRepository expenseTransactionRepository;
    private final IncomeTransactionRepository incomeTransactionRepository;
    private final TransferTransactionRepository transferTransactionRepository;

    @Override
    public TagsResponseDto getTagById(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found!"));
        return modelMapper.map(tag, TagsResponseDto.class);
    }

    @Override
    public TagsResponseDto addTag(TagRequestDto tagRequestDto, UUID userId) {
        if (tagRepository.existsByNameAndUserId(tagRequestDto.getName(), userId)) {
            throw new AlreadyExistsException(tagRequestDto.getName() + " already exists");
        }

        Tag tag = modelMapper.map(tagRequestDto, Tag.class);
        User user = new User();
        user.setId(userId);
        tag.setUser(user);

        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagsResponseDto.class);
    }

    @Override
    public TagsResponseDto updateTag(TagRequestDto tagRequestDto, UUID id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found!"));

        existingTag.setName(tagRequestDto.getName());

        Tag updatedTag = tagRepository.save(existingTag);
        return modelMapper.map(updatedTag, TagsResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteTagById(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found!"));

        // Remove from Expense Transactions
        List<ExpenseTransaction> expenseTransactions = expenseTransactionRepository.findByTags_Id(id);
        for (ExpenseTransaction transaction : expenseTransactions) {
            transaction.getTags().remove(tag);
        }
        expenseTransactionRepository.saveAll(expenseTransactions);

        // Remove from Income Transactions
        List<IncomeTransaction> incomeTransactions = incomeTransactionRepository.findByTags_Id(id);
        for (IncomeTransaction transaction : incomeTransactions) {
            transaction.getTags().remove(tag);
        }
        incomeTransactionRepository.saveAll(incomeTransactions);

        // Remove from Transfer Transactions
        List<TransferTransaction> transferTransactions = transferTransactionRepository.findByTags_Id(id);
        for (TransferTransaction transaction : transferTransactions) {
            transaction.getTags().remove(tag);
        }
        transferTransactionRepository.saveAll(transferTransactions);

        tagRepository.delete(tag);
    }

    @Override
    public List<TagsResponseDto> getAllTagsByUser(UUID userId) {
        return tagRepository.findByUserId(userId)
                .stream()
                .map(tag -> {
                    TagsResponseDto dto = modelMapper.map(tag, TagsResponseDto.class);
                    int count = countTransactionsWithTag(tag.getId());
                    dto.setTransactions(count);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private int countTransactionsWithTag(UUID tagId) {
        int expenseCount = expenseTransactionRepository.countByTags_Id(tagId);
        int incomeCount = incomeTransactionRepository.countByTags_Id(tagId);
        int transferCount = transferTransactionRepository.countByTags_Id(tagId);
        return expenseCount + incomeCount + transferCount;
    }

    @Override
    @Transactional
    public void mergeTags(UUID sourceId, UUID targetId) {
        Tag source = tagRepository.findById(sourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Source tag not found!"));
        Tag target = tagRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("Target tag not found!"));

        // Update Expense Transactions
        List<ExpenseTransaction> expenseTransactions = expenseTransactionRepository.findByTags_Id(sourceId);
        for (ExpenseTransaction transaction : expenseTransactions) {
            transaction.getTags().remove(source);
            transaction.getTags().add(target);
        }
        expenseTransactionRepository.saveAll(expenseTransactions);

        // Update Income Transactions
        List<IncomeTransaction> incomeTransactions = incomeTransactionRepository.findByTags_Id(sourceId);
        for (IncomeTransaction transaction : incomeTransactions) {
            transaction.getTags().remove(source);
            transaction.getTags().add(target);
        }
        incomeTransactionRepository.saveAll(incomeTransactions);

        // Update Transfer Transactions
        List<TransferTransaction> transferTransactions = transferTransactionRepository.findByTags_Id(sourceId);
        for (TransferTransaction transaction : transferTransactions) {
            transaction.getTags().remove(source);
            transaction.getTags().add(target);
        }
        transferTransactionRepository.saveAll(transferTransactions);

        tagRepository.delete(source);
    }

    public Set<Tag> resolveTags(List<UUID> tagIds, List<String> names) {
        UUID uid = securityUtil.getAuthenticatedUserId();
        Set<Tag> tags = new HashSet<>();
        if (tagIds != null) {
            tags.addAll(tagRepository.findAllById(tagIds));
        }
        if (names != null) {
            for (String nm : names) {
                Tag tg = tagRepository.findByNameAndUserId(nm.trim(), uid)
                        .orElseGet(() -> {
                            Tag newT = new Tag();
                            newT.setName(nm.trim());
                            newT.setUser(new User() {{
                                setId(uid);
                            }});
                            return tagRepository.save(newT);
                        });
                tags.add(tg);
            }
        }
        return tags;
    }
}
