package com.expensetrace.app.service.tag;

import com.expensetrace.app.dto.response.tag.TagResponseDto;
import com.expensetrace.app.dto.response.tag.TagsResponseDto;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.transaction.TaggableTransaction;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.repository.transaction.TransactionRepository;
import com.expensetrace.app.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<TagsResponseDto> getAllTags(Pageable pageable) {
        UUID userId = userService.getAuthenticatedUser().getId();
        return tagRepository.findAllByUserId(userId, pageable)
                .map(tag -> {
                    TagsResponseDto dto = new TagsResponseDto();
                    dto.setTag(modelMapper.map(tag, com.expensetrace.app.dto.response.tag.TagResponseDto.class));
                    dto.setTransactions(tag.getId() != null ? tag.getTransactionsCount() : 0);
                    return dto;
                });
    }

    @Override
    @Transactional
    public void deleteTag(UUID tagId) {
        UUID userId = userService.getAuthenticatedUser().getId();
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        if (!tag.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }

        // Remove relationships with transactions (CASCADE will take care if configured)
        for (TaggableTransaction txn : tag.getTransactions()) {
            txn.getTags().remove(tag);
        }

        tagRepository.delete(tag);
    }

    @Override
    @Transactional
    public void mergeTags(UUID sourceTagId, UUID targetTagId) {
        UUID userId = userService.getAuthenticatedUser().getId();
        if (sourceTagId.equals(targetTagId)) {
            throw new IllegalArgumentException("Source and target tags cannot be the same");
        }

        Tag source = tagRepository.findById(sourceTagId)
                .orElseThrow(() -> new EntityNotFoundException("Source tag not found"));

        Tag target = tagRepository.findById(targetTagId)
                .orElseThrow(() -> new EntityNotFoundException("Target tag not found"));

        if (!source.getUser().getId().equals(userId) || !target.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }

        // Reassign all transactions from source → target
        for (TaggableTransaction txn : source.getTransactions()) {
            txn.getTags().remove(source);
            txn.getTags().add(target);
        }

        tagRepository.delete(source);
    }
}