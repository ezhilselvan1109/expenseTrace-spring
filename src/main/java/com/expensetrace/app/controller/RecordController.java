package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.RecordRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.RecordResponseDto;
import com.expensetrace.app.service.record.IRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/records")
@Tag(name = " Records", description = "manage records")
public class RecordController {
    private final IRecordService debtRecordService;

    @PostMapping("/{debtId}")
    @Operation(summary = "Add a new debt Record", description = "Create a new debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> addRecord(@PathVariable UUID debtId, @RequestBody RecordRequestDto recordRequestDto) {
        try {
            RecordResponseDto debt = debtRecordService.createRecord(debtId, recordRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", debt));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "update debt Record", description = "update debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> updateRecord(@PathVariable UUID id, @RequestBody RecordRequestDto recordRequestDto) {
        try {
            RecordResponseDto updated = debtRecordService.updateRecord(id, recordRequestDto);
            return ResponseEntity.ok(new ApiResponse(" updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "get debt Record", description = "get debt Record by id for the authenticated user")
    public ResponseEntity<ApiResponse> getRecord(@PathVariable UUID id) {
        try {
            RecordResponseDto debtRecord = debtRecordService.getRecordById(id);
            return ResponseEntity.ok(new ApiResponse(" found", debtRecord));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/id/{debtId}")
    @Operation(summary = "get all debt Record", description = "get all debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> getAllRecords(@PathVariable UUID debtId) {
        List<RecordResponseDto> debts = debtRecordService.getAllRecordsByUser(debtId);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @GetMapping("/{debtId}/all")
    @Operation(summary = "get all debt Record with pagination", description = "get all debt Record with pagination for the authenticated user")
    public ResponseEntity<ApiResponse> getAllRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<RecordResponseDto> debts = debtRecordService.getAllRecordsByUser(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }
    @GetMapping("/{debtId}/paid")
    @Operation(summary = "get all paid debt Record", description = "get all paid debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> getAllPaidRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Page<RecordResponseDto> debts = debtRecordService.getAllPaidRecordsByUser(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @GetMapping("/{debtId}/adjustment")
    @Operation(summary = "get all adjustment debt Record", description = "get all adjustment debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> getAllAdjustmentRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        Page<RecordResponseDto> debts = debtRecordService.getAllAdjustmentRecordsByUser(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @GetMapping("/id/{debtId}/received")
    @Operation(summary = "get all received debt Record", description = "get all received debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> getAllReceivedRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<RecordResponseDto> debts = debtRecordService.getAllReceivedRecordsByUser(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete debt Record", description = "delete debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> deleteRecord(@PathVariable UUID id) {
        try {
            debtRecordService.deleteRecordById(id);
            return ResponseEntity.ok(new ApiResponse(" deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/total-paid")
    @Operation(summary = "Get total-paid")
    public ResponseEntity<ApiResponse> getTotalPaid() {
        return ResponseEntity.ok(new ApiResponse("Fetched", null));
    }

    @GetMapping("/total-received")
    @Operation(summary = "Get total received")
    public ResponseEntity<ApiResponse> getTotalReceived() {
        return ResponseEntity.ok(new ApiResponse("Fetched", null));
    }
}
