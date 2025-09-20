package com.expensetrace.app.controller;

import com.expensetrace.app.enums.AnalysisType;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.summary.*;
import com.expensetrace.app.service.AnalysisService;
import com.expensetrace.app.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/summary")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Summary", description = "Endpoints for managing summary")
public class SummaryController {

    private final AnalysisService analysisService;
    private final SummaryService summaryService;

    @Operation(summary = "Get summary for the authenticated user", description = "Get summary for the currently authenticated user.")
    @GetMapping("/analysis")
    public ResponseEntity<ApiResponse> getAnalysis(
            @RequestParam Integer type,  // WEEK, MONTH, YEAR, CUSTOM
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(new ApiResponse("Found!",analysisService.getAnalysis(AnalysisType.fromCode(type), year, month, from, to)));
    }

    @Operation(summary = "Get summary by day for the authenticated user", description = "Get summary by day for the currently authenticated user.")
    @GetMapping("/day")
    public ResponseEntity<ApiResponse> getDaySummary(@RequestParam Integer day, @RequestParam Integer
            month, @RequestParam Integer year) {
        try {
            DaySummaryResponseDto daySummaryResponseDto = new DaySummaryResponseDto();
            daySummaryResponseDto.setIncome(5000);
            daySummaryResponseDto.setSpending(5000);
            return ResponseEntity.ok(new ApiResponse("Found!", daySummaryResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "Get summary by month for the authenticated user", description = "Get summary by month for the currently authenticated user.")
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse> getMonthlySummary(@RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(new ApiResponse("Found!", summaryService.getMonthlySummary(year, month)));
    }
}
