package com.expensetrace.app.controller.transaction;

import com.expensetrace.app.dto.request.schedule.ScheduleExpenseRequestDto;
import com.expensetrace.app.dto.request.schedule.ScheduleIncomeRequestDto;
import com.expensetrace.app.dto.request.schedule.ScheduleTransferRequestDto;
import com.expensetrace.app.dto.response.schedule.ScheduleBaseResponseDto;
import com.expensetrace.app.dto.response.schedule.ScheduleExpenseResponseDto;
import com.expensetrace.app.dto.response.schedule.ScheduleIncomeResponseDto;
import com.expensetrace.app.dto.response.schedule.ScheduleTransferResponseDto;
import com.expensetrace.app.enums.schedule.ExecutionStatus;
import com.expensetrace.app.enums.schedule.ScheduleType;
import com.expensetrace.app.service.schedule.ScheduledTransactionBaseService;
import com.expensetrace.app.service.schedule.ScheduledTransactionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduledTransactionController {

    private final ScheduledTransactionFactory factory;
    private final ScheduledTransactionBaseService baseService;

    @PostMapping("/expense")
    public ResponseEntity<ScheduleExpenseResponseDto> createExpense(
            @RequestBody ScheduleExpenseRequestDto request) {
        var service = factory.<ScheduleExpenseRequestDto, ScheduleExpenseResponseDto>
                getService(ScheduleType.EXPENSE);
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/expense")
    public ResponseEntity<ScheduleExpenseResponseDto> updateExpense(
            @PathVariable UUID id,
            @RequestBody ScheduleExpenseRequestDto request) {
        var service = factory.<ScheduleExpenseRequestDto, ScheduleExpenseResponseDto>
                getService(ScheduleType.EXPENSE);
        return ResponseEntity.ok(service.update(id,request));
    }

    @PostMapping("/incoming")
    public ResponseEntity<ScheduleIncomeResponseDto> createIncoming(
            @RequestBody ScheduleIncomeRequestDto request) {
        var service = factory.<ScheduleIncomeRequestDto, ScheduleIncomeResponseDto>
                getService(ScheduleType.INCOME);
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/incoming")
    public ResponseEntity<ScheduleIncomeResponseDto> updateIncoming(
            @PathVariable UUID id,
            @RequestBody ScheduleIncomeRequestDto request) {
        var service = factory.<ScheduleIncomeRequestDto, ScheduleIncomeResponseDto>
                getService(ScheduleType.INCOME);
        return ResponseEntity.ok(service.update(id,request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ScheduleTransferResponseDto> createTransfer(
            @RequestBody ScheduleTransferRequestDto request) {
        var service = factory.<ScheduleTransferRequestDto, ScheduleTransferResponseDto>
                getService(ScheduleType.TRANSFER);
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/transfer")
    public ResponseEntity<ScheduleTransferResponseDto> updateTransfer(
            @PathVariable UUID id,
            @RequestBody ScheduleTransferRequestDto request) {
        var service = factory.<ScheduleTransferRequestDto, ScheduleTransferResponseDto>
                getService(ScheduleType.TRANSFER);
        return ResponseEntity.ok(service.update(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        baseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleBaseResponseDto> getById(@PathVariable UUID id) {
        var dto = baseService.getById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<ScheduleBaseResponseDto>> getAllByExecutionStatusUpcoming(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(baseService.getAllByExecutionStatus(ExecutionStatus.UPCOMING, page, size));
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<ScheduleBaseResponseDto>> getAllByExecutionStatusCompleted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(baseService.getAllByExecutionStatus(ExecutionStatus.COMPLETED, page, size));
    }
}

