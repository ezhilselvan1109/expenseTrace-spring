package com.expensetrace.app.controller;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.enums.CategoryType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.dto.response.summary.*;
import com.expensetrace.app.dto.response.TransactionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/summary")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Summary", description = "Endpoints for managing summary")
public class SummaryController {

    @Operation(summary = "Get summary for the authenticated user", description = "Get summary for the currently authenticated user.")
    @GetMapping("/analysis")
    public ResponseEntity<ApiResponse> getAnalysis(@RequestParam String type,
                                                   @RequestParam(required = false) Integer date,
                                                   @RequestParam(required = false) Integer month,
                                                   @RequestParam(required = false) Integer year,
                                                   @RequestParam(required = false) Integer fromDate,
                                                   @RequestParam(required = false) Integer fromMonth,
                                                   @RequestParam(required = false) Integer fromYear,
                                                   @RequestParam(required = false) Integer toDate,
                                                   @RequestParam(required = false) Integer toMonth,
                                                   @RequestParam(required = false) Integer toYear
    ) {
        try {
            // Example: Validate inputs based on type
            LocalDate startDate;
            LocalDate endDate;

            /*
            switch (type.toLowerCase()) {
                case "week" -> {
                    if (date == null || month == null || year == null)
                        throw new IllegalArgumentException("Week requires date, month, year");
                    LocalDate inputDate = LocalDate.of(year, month, date);
                    startDate = inputDate.with(DayOfWeek.MONDAY);
                    endDate = inputDate.with(DayOfWeek.SUNDAY);
                }
                case "month" -> {
                    if (month == null || year == null)
                        throw new IllegalArgumentException("Month requires month and year");
                    startDate = LocalDate.of(year, month, 1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                }
                case "year" -> {
                    if (year == null)
                        throw new IllegalArgumentException("Year is required");
                    startDate = LocalDate.of(year, 1, 1);
                    endDate = LocalDate.of(year, 12, 31);
                }
                case "custom" -> {
                    if (fromDate == null || fromMonth == null || fromYear == null || toDate == null || toMonth == null || toYear == null)
                        throw new IllegalArgumentException("Custom requires from/to full date values");
                    startDate = LocalDate.of(fromYear, fromMonth, fromDate);
                    endDate = LocalDate.of(toYear, toMonth, toDate);
                }
                default -> throw new IllegalArgumentException("Invalid type: " + type);
            }
            */

            AnalysisSummaryResponseDto analysisSummaryResponse = new AnalysisSummaryResponseDto();
            analysisSummaryResponse.setIncome(100);
            analysisSummaryResponse.setSpending(100);
            analysisSummaryResponse.setNumberOfTransactions(200);
            analysisSummaryResponse.setAverageSpendingPerDay(200);
            analysisSummaryResponse.setAverageSpendingPerTransaction(200);
            analysisSummaryResponse.setAverageIncomePerDay(200);
            analysisSummaryResponse.setAverageIncomePerTransaction(200);
            analysisSummaryResponse.setAverageIncomePerDay(200);

            List<AccountSummary> incomeAccount = new ArrayList<>();
            List<AccountSummary> spendingAccount = new ArrayList<>();
            List<AccountSummary> transfersAccount = new ArrayList<>();

            AccountSummary BankAccountSummary = new AccountSummary();
            AccountResponseDto BankAccountResponseDto=new AccountResponseDto();
            BankAccountResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c66"));
            BankAccountResponseDto.setName("Bank");
            BankAccountResponseDto.setType(AccountType.fromCode(1));
            BankAccountResponseDto.setDefault(false);
            BankAccountSummary.setAccountResponseDto(BankAccountResponseDto);
            BankAccountSummary.setAmount(500);

            incomeAccount.add(BankAccountSummary);
            spendingAccount.add(BankAccountSummary);
//            transfersAccount.add(BankAccountSummary);

            AccountSummary CardAccountSummary = new AccountSummary();
            AccountResponseDto CardAccountResponseDto=new AccountResponseDto();
            CardAccountResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c76"));
            CardAccountResponseDto.setName("Card");
            CardAccountResponseDto.setType(AccountType.fromCode(2));
            CardAccountResponseDto.setDefault(false);
            CardAccountSummary.setAccountResponseDto(CardAccountResponseDto);
            CardAccountSummary.setAmount(300);

            incomeAccount.add(CardAccountSummary);
            spendingAccount.add(CardAccountSummary);
//            transfersAccount.add(CardAccountSummary);

            AccountSummary cashAccountSummary = new AccountSummary();
            AccountResponseDto cashAccountResponseDto=new AccountResponseDto();
            cashAccountResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c86"));
            cashAccountResponseDto.setName("cash");
            cashAccountResponseDto.setType(AccountType.fromCode(1));
            cashAccountResponseDto.setDefault(false);
            cashAccountSummary.setAccountResponseDto(cashAccountResponseDto);
            cashAccountSummary.setAmount(600);

            incomeAccount.add(cashAccountSummary);
            spendingAccount.add(cashAccountSummary);
//            transfersAccount.add(cashAccountSummary);

            analysisSummaryResponse.setIncomeAccount(incomeAccount);
            analysisSummaryResponse.setSpendingAccount(spendingAccount);
            analysisSummaryResponse.setTransfersAccount(transfersAccount);


            List<CategorySummary> spendingCategory = new ArrayList<>();
            List<CategorySummary> incomeCategory = new ArrayList<>();

            CategorySummary foodCategorySummary = new CategorySummary();
            CategoryResponseDto foodCategoryResponseDto=new CategoryResponseDto();

            foodCategoryResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c86"));
            foodCategoryResponseDto.setType(CategoryType.fromCode(1));
            foodCategoryResponseDto.setName("Food");
            foodCategoryResponseDto.setIcon("apple");
            foodCategoryResponseDto.setColor("red");
            foodCategorySummary.setCategory(foodCategoryResponseDto);
            foodCategorySummary.setAmount(100);

            spendingCategory.add(foodCategorySummary);

            CategorySummary TravelCategorySummary = new CategorySummary();
            CategoryResponseDto TravelCategoryResponseDto=new CategoryResponseDto();
            TravelCategoryResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c88"));
            TravelCategoryResponseDto.setType(CategoryType.fromCode(2));
            TravelCategoryResponseDto.setName("Travel");
            TravelCategoryResponseDto.setIcon("airplane");
            TravelCategoryResponseDto.setColor("blue");
            TravelCategorySummary.setCategory(TravelCategoryResponseDto);
            TravelCategorySummary.setAmount(400);

            incomeCategory.add(TravelCategorySummary);

            CategorySummary MedicalCategorySummary = new CategorySummary();
            CategoryResponseDto MedicalCategoryResponseDto=new CategoryResponseDto();
            MedicalCategoryResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c89"));
            MedicalCategoryResponseDto.setType(CategoryType.fromCode(1));
            MedicalCategoryResponseDto.setName("Medical");
            MedicalCategoryResponseDto.setIcon("pill");
            MedicalCategoryResponseDto.setColor("teal");
            MedicalCategorySummary.setCategory(MedicalCategoryResponseDto);
            MedicalCategorySummary.setAmount(700);

            spendingCategory.add(MedicalCategorySummary);

            CategorySummary ShoppingCategorySummary = new CategorySummary();
            CategoryResponseDto ShoppingCategoryResponseDto=new CategoryResponseDto();
            ShoppingCategoryResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-54b8fd0c6c89"));
            ShoppingCategoryResponseDto.setType(CategoryType.fromCode(1));
            ShoppingCategoryResponseDto.setName("Shopping");
            ShoppingCategoryResponseDto.setIcon("shopping-bag");
            ShoppingCategoryResponseDto.setColor("violet");
            ShoppingCategorySummary.setCategory(ShoppingCategoryResponseDto);
            ShoppingCategorySummary.setAmount(700);

            incomeCategory.add(ShoppingCategorySummary);

            CategorySummary FamilyCategorySummary = new CategorySummary();
            CategoryResponseDto FamilyCategoryResponseDto=new CategoryResponseDto();
            FamilyCategoryResponseDto.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd8c6c89"));
            FamilyCategoryResponseDto.setType(CategoryType.fromCode(1));
            FamilyCategoryResponseDto.setName("Family");
            FamilyCategoryResponseDto.setIcon("home");
            FamilyCategoryResponseDto.setColor("orange");
            FamilyCategorySummary.setCategory(FamilyCategoryResponseDto);
            FamilyCategorySummary.setAmount(700);

            spendingCategory.add(FamilyCategorySummary);
            incomeCategory.add(FamilyCategorySummary);

            analysisSummaryResponse.setSpendingCategory(spendingCategory);
            analysisSummaryResponse.setIncomeCategory(incomeCategory);

            return ResponseEntity.ok(new ApiResponse("Success", analysisSummaryResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "Get summary by day for the authenticated user", description = "Get summary by day for the currently authenticated user.")
    @GetMapping("/day")
    public ResponseEntity<ApiResponse> getDaySummary(@RequestParam Integer day, @RequestParam Integer
            month, @RequestParam Integer year) {
        try {
            List<TransactionResponseDto> transactionResponse = new ArrayList<>();
            TransactionResponseDto transactionResponseDto1 = new TransactionResponseDto();
            transactionResponseDto1.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c66"));
            transactionResponseDto1.setAmount(BigDecimal.valueOf(500));
            transactionResponse.add(transactionResponseDto1);

            TransactionResponseDto transactionResponseDto2 = new TransactionResponseDto();
            transactionResponseDto2.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c66"));
            transactionResponseDto2.setAmount(BigDecimal.valueOf(500));
            transactionResponse.add(transactionResponseDto2);

            DaySummaryResponseDto daySummaryResponseDto = new DaySummaryResponseDto();
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
        List<MonthSummaryResponseDto> monthSummaryResponse = new ArrayList<>();
        MonthSummaryResponseDto monthSummaryResponseDto = new MonthSummaryResponseDto();
        monthSummaryResponseDto.setDay(2);
        monthSummaryResponseDto.setExpense(500);
        monthSummaryResponseDto.setIncome(600);
        monthSummaryResponse.add(monthSummaryResponseDto);

        MonthSummaryResponseDto monthSummaryResponseDto2 = new MonthSummaryResponseDto();
        monthSummaryResponseDto2.setDay(2);
        monthSummaryResponseDto2.setExpense(500);
        monthSummaryResponseDto2.setIncome(600);
        monthSummaryResponse.add(monthSummaryResponseDto2);

        MonthSummaryResponseDto monthSummaryResponseDto3 = new MonthSummaryResponseDto();
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
