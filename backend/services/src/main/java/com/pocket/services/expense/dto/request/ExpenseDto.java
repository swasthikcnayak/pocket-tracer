package com.pocket.services.expense.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pocket.services.expense.model.Category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class ExpenseDto {

    @NotNull(message = "amount cannot be null")
    @DecimalMin(value = "0.01", message = "amount must be atleast 0.01")
    private double amount;

    @NotNull(message = "title cannot be null")
    @NotEmpty(message = "title cannot be empty")
    private String title;

    @NotNull(message = "category cannot be null")
    private Category category;

    @NotNull(message = "date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "date has to be past or present")
    private LocalDateTime date;

    private String description;
}
