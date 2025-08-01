package com.expensetrace.app.service.tag;

import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.dto.request.TagRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.dto.response.TagResponseDto;
import com.expensetrace.app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public TagResponseDto getTagById(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found!"));
        return modelMapper.map(tag, TagResponseDto.class);
    }

    @Override
    public TagResponseDto addTag(TagRequestDto tagRequestDto,UUID userId) {
        if (tagRepository.existsByNameAndUserId(tagRequestDto.getName(),userId)) {
            throw new AlreadyExistsException(tagRequestDto.getName() + " already exists");
        }

        Tag tag = modelMapper.map(tagRequestDto, Tag.class);
        User user=new User();
        user.setId(userId);
        tag.setUser(user);

        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagResponseDto.class);
    }

    @Override
    public TagResponseDto updateTag(TagRequestDto tagRequestDto, UUID id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found!"));

        existingTag.setName(tagRequestDto.getName());

        Tag updatedTag = tagRepository.save(existingTag);
        return modelMapper.map(updatedTag, TagResponseDto.class);
    }


    @Override
    public void deleteTagById(UUID id) {
        tagRepository.findById(id)
                .ifPresentOrElse(tagRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }

    @Override
    public List<TagResponseDto> getAllTagsByUser(UUID userId) {
        return tagRepository.findByUserId(userId)
                .stream()
                .map(tag -> {
                    TagResponseDto dto = modelMapper.map(tag, TagResponseDto.class);
                    int count = transactionRepository.countByTags_Id(tag.getId());
                    dto.setTransactions(count);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void mergeTags(UUID sourceId, UUID targetId) {
        Tag source = tagRepository.findById(sourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Source tag not found!"));
        Tag target = tagRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("Target tag not found!"));

        List<Transaction> transactions = transactionRepository.findByTags_Id(sourceId);

        for (Transaction transaction : transactions) {
            Set<Tag> tags = transaction.getTags();
            tags.remove(source);
            tags.add(target);
        }

        transactionRepository.saveAll(transactions);
        tagRepository.delete(source);
    }
}
