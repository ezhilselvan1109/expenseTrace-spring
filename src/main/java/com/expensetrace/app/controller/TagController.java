package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.TagRequestDto;
import com.expensetrace.app.dto.response.tag.TagsResponseDto;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.tag.ITagService;
import com.expensetrace.app.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Endpoints for managing tags")
public class TagController {
    private final ITagService tagService;
    private final SecurityUtil securityUtil;

    @Operation(summary = "Get all tags for the authenticated user", description = "Returns a list of all tags created by the currently authenticated user.")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTags() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        try {
            List<TagsResponseDto> tags = tagService.getAllTagsByUser(userId);
            return ResponseEntity.ok(new ApiResponse("Tags fetched successfully!", tags));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error fetching tags", null));
        }
    }

    @Operation(summary = "Delete a tag by ID", description = "Deletes the tag with the specified ID. Throws an error if the tag is not found.")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable UUID id) {
        try {
            tagService.deleteTagById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "merge a tag by ID", description = "Merges the tag with the specified ID into another tag identified by tagId parameter. Throws an error if either tag is not found.")
    @PutMapping("/{id}/merge")
    public ResponseEntity<ApiResponse> mergeTag(@PathVariable UUID id, @RequestParam UUID tagId) {
        try {
            tagService.mergeTags(id, tagId);
            return ResponseEntity.ok(new ApiResponse("Merged successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Update a tag by ID", description = "Updates the tag with the specified ID using the provided details. Throws an error if the tag is not found.")
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateTag(@PathVariable UUID id, @Valid @RequestBody TagRequestDto tag) {
        try {
            TagsResponseDto updatedTagResponseDto = tagService.updateTag(tag, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedTagResponseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
