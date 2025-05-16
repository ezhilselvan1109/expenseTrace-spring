package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.TagResponseDto;
import com.expensetrace.app.service.tag.ITagService;
import com.expensetrace.app.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Get all tags for the authenticated user",
            description = "Returns a list of all tags created by the currently authenticated user."
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTagsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        try {
            List<TagResponseDto> tagResponseDto = tagService.getAllTagsByUser(userId);
            return ResponseEntity.ok(new ApiResponse("Found!", tagResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(
            summary = "Create a new tag",
            description = "Adds a new tag for the authenticated user. Throws an error if the tag already exists."
    )
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addTag(@RequestBody TagRequestDto tagRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        try {
            TagResponseDto theTagResponseDto = tagService.addTag(tagRequestDto, userId);
            return ResponseEntity.ok(new ApiResponse("Success", theTagResponseDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get a tag by ID",
            description = "Fetches a specific tag by its ID. Throws an error if the tag is not found."
    )
    @GetMapping("/tag/{id}")
    public ResponseEntity<ApiResponse> getTagById(@PathVariable UUID id) {
        try {
            TagResponseDto theTagResponseDto = tagService.getTagById(id);
            return ResponseEntity.ok(new ApiResponse("Found", theTagResponseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Delete a tag by ID",
            description = "Deletes the tag with the specified ID. Throws an error if the tag is not found."
    )
    @DeleteMapping("/tag/{id}/delete")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable UUID id) {
        try {
            tagService.deleteTagById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update a tag by ID",
            description = "Updates the tag with the specified ID using the provided details. Throws an error if the tag is not found."
    )
    @PutMapping("/tag/{id}/update")
    public ResponseEntity<ApiResponse> updateTag(@PathVariable UUID id, @RequestBody TagRequestDto tag) {
        try {
            TagResponseDto updatedTagResponseDto = tagService.updateTag(tag, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedTagResponseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
