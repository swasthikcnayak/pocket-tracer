package com.pocket.services.income.dto.request;

import java.util.Date;

import com.pocket.services.income.model.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class IncomeDto {

    @NotNull(message="Amount cannot be null")
    @NotEmpty(message="Amount cannot be empty")
    @DecimalMin("0.1") 
    private double amount;

    @NotNull(message="Amount cannot be null")
    @NotEmpty(message="Amount cannot be empty")
    private String title;

    @NotNull(message="Category cannot be null")
    @NotEmpty(message="Category cannot be empty")
    private Category category;
    
    @NotNull(message="Date cannot be null")
    @NotEmpty(message="Date cannot be empty")
    private Date date;

    private String description;
}
