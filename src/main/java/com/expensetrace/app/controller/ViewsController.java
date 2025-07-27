package com.expensetrace.app.controller;

import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.DaySummaryResponseDto;
import com.expensetrace.app.responseDto.MonthSummaryResponseDto;
import com.expensetrace.app.responseDto.TransactionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/views")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Views", description = "Endpoints for managing views")
public class ViewsController {

    @Operation(summary = "Get summary by day for the authenticated user", description = "Get summary by day for the currently authenticated user.")
    @GetMapping("/day")
    public ResponseEntity<ApiResponse> getDaySummary(@RequestParam Integer day, @RequestParam Integer month, @RequestParam Integer year) {
        try {
            List<TransactionResponseDto> transactionResponse = new ArrayList<>();
            TransactionResponseDto transactionResponseDto1=new TransactionResponseDto();
            transactionResponseDto1.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c66"));
            transactionResponseDto1.setAmount(BigDecimal.valueOf(500));
            transactionResponse.add(transactionResponseDto1);

            TransactionResponseDto transactionResponseDto2=new TransactionResponseDto();
            transactionResponseDto2.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c66"));
            transactionResponseDto2.setAmount(BigDecimal.valueOf(500));
            transactionResponse.add(transactionResponseDto2);

            DaySummaryResponseDto daySummaryResponseDto=new DaySummaryResponseDto();
            daySummaryResponseDto.setIncome(5000);
            daySummaryResponseDto.setSpending(5000);
            daySummaryResponseDto.setTransactions(transactionResponse);
            return ResponseEntity.ok(new ApiResponse("Found!", daySummaryResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "Get summary by month for the authenticated user", description = "Get summary by month for the currently authenticated user.")
    @GetMapping("/month")
    public ResponseEntity<ApiResponse> getMonthSummary(@RequestParam Integer month, @RequestParam Integer year) {
        List<MonthSummaryResponseDto> monthSummaryResponse=new ArrayList<>();
        MonthSummaryResponseDto monthSummaryResponseDto=new MonthSummaryResponseDto();
        monthSummaryResponseDto.setDay(2);
        monthSummaryResponseDto.setExpense(500);
        monthSummaryResponseDto.setIncome(600);
        monthSummaryResponse.add(monthSummaryResponseDto);

        MonthSummaryResponseDto monthSummaryResponseDto2=new MonthSummaryResponseDto();
        monthSummaryResponseDto2.setDay(2);
        monthSummaryResponseDto2.setExpense(500);
        monthSummaryResponseDto2.setIncome(600);
        monthSummaryResponse.add(monthSummaryResponseDto2);

        MonthSummaryResponseDto monthSummaryResponseDto3=new MonthSummaryResponseDto();
        monthSummaryResponseDto3.setDay(2);
        monthSummaryResponseDto3.setExpense(500);
        monthSummaryResponseDto3.setIncome(600);
        monthSummaryResponse.add(monthSummaryResponseDto3);
        try {
            return ResponseEntity.ok(new ApiResponse("Fetch successfully", monthSummaryResponse));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
