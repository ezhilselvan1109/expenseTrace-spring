package com.expensetrace.app.responseDto.summary;

import java.util.List;
import lombok.Data;

@Data
public class AnalysisSummaryResponseDto {
    private Integer spending;
    private Integer income;
    private List<CategorySummary> spendingCategory;
    private List<CategorySummary> incomeCategory;
    private List<AccountSummary> incomeAccount;
    private List<AccountSummary> spendingAccount;
    private List<AccountSummary> transfersAccount;
    private Integer numberOfTransactions;
    private Integer averageSpendingPerDay;
    private Integer averageSpendingPerTransaction;
    private Integer averageIncomePerDay;
    private Integer averageIncomePerTransaction;
}
