package com.pocket.services.budget.dto.request;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class BudgetDto {

    @NotNull(message = "Month cannot be null")
    @Max(12)
    @Min(1)
    private int month;

    @NotNull(message = "Year cannot be null")
    private int year;

    @NotNull(message = "Budget entries cannot be null")
    @NotEmpty(message = "Budget entries cannot be empty")
    List<BudgetEntryDto> budgetEntries;
}
