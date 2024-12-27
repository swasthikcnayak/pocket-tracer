package com.pocket.services.budget.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetResponseDto {
    private int id;
    private int month;
    private int year;
    private List<BudgetEntryResponseDto> budgetEntries;
}
