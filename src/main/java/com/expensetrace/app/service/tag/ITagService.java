package com.expensetrace.app.service.tag;

import com.expensetrace.app.dto.request.TagRequestDto;
import com.expensetrace.app.dto.response.TagResponseDto;

import java.util.List;
import java.util.UUID;

public interface ITagService {
    TagResponseDto getTagById(UUID id);
    List<TagResponseDto> getAllTagsByUser(UUID userId);
    TagResponseDto addTag(TagRequestDto tagRequestDto,UUID userId);
    TagResponseDto updateTag(TagRequestDto tagRequestDto, UUID id);
    void deleteTagById(UUID id);
}
