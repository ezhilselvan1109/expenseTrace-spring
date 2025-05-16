package com.expensetrace.app.service.tag;

import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.responseDto.TagResponseDto;

import java.util.List;
import java.util.UUID;

public interface ITagService {
    TagResponseDto getTagById(UUID id);
    List<TagResponseDto> getAllTagsByUser(UUID userId);
    TagResponseDto addTag(TagRequestDto tagRequestDto,UUID userId);
    TagResponseDto updateTag(TagRequestDto tagRequestDto, UUID id);
    void deleteTagById(UUID id);
}
