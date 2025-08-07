package com.expensetrace.app.service.tag;

import com.expensetrace.app.dto.request.TagRequestDto;
import com.expensetrace.app.dto.response.tag.TagsResponseDto;
import com.expensetrace.app.model.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ITagService {
    TagsResponseDto getTagById(UUID id);
    List<TagsResponseDto> getAllTagsByUser(UUID userId);
    TagsResponseDto addTag(TagRequestDto tagRequestDto,UUID userId);
    TagsResponseDto updateTag(TagRequestDto tagRequestDto, UUID id);
    void deleteTagById(UUID id);
    void mergeTags(UUID sourceId, UUID targetId);
    Set<Tag> resolveTags(List<UUID> tagIds, List<String> names);
}
