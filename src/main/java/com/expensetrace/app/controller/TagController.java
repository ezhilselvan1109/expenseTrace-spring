package com.expensetrace.app.controller;

import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTags(
            Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",tagService.getAllTags(pageable)));
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable UUID tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/merge")
    public ResponseEntity<Void> mergeTags(
            @RequestParam UUID sourceTagId,
            @RequestParam UUID targetTagId) {
        tagService.mergeTags(sourceTagId, targetTagId);
        return ResponseEntity.ok().build();
    }
}