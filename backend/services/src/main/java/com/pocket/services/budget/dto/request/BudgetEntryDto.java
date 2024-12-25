package com.pocket.services.budget.dto.request;

import com.pocket.services.budget.model.Category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetEntryDto {
    @NotNull(message = "category cannot be null")
    private Category category;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin("0.1")
    private double amount;
}
