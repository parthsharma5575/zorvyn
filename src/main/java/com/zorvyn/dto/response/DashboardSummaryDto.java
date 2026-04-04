package com.zorvyn.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record DashboardSummaryDto(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal netBalance,
        Map<String, BigDecimal> categoryTotals,
        List<TransactionResponseDto> recentTransactions
){}
