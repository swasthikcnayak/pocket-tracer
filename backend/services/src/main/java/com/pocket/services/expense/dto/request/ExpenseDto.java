package com.pocket.services.expense.dto.request;

import java.time.LocalDate;

import com.pocket.services.expense.model.Category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data

public class ExpenseDto {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin("0.1")
    private double amount;

    @NotNull(message = "Amount cannot be null")
    @NotEmpty(message = "Amount cannot be empty")
    private String title;

    @NotNull(message = "Category cannot be null")
    private Category category;

    @NotNull(message = "date cannot be empty")
    @PastOrPresent(message = "Date has to be past or present")
    private LocalDate date;

    private String description;
}
