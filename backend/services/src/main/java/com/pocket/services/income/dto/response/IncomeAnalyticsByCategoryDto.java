package com.pocket.services.income.dto.response;

import com.pocket.services.income.model.Category;

import lombok.Data;

@Data
public class IncomeAnalyticsByCategoryDto {
    private Category category;
    private double totalAmount;

    IncomeAnalyticsByCategoryDto(String categoryString, double totalAmount) {
        this.totalAmount = totalAmount;
        this.category = Category.valueOf(categoryString);
    }

}
