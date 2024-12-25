package com.pocket.services.budget.dto.response;

import com.pocket.services.budget.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class BudgetEntryResponseDto {
    private Category category;
    private double amount;
}
