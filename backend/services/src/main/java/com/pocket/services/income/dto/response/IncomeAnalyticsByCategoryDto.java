package com.pocket.services.income.dto.response;

import com.pocket.services.income.model.Category;

import lombok.Data;

@Data
public class IncomeAnalyticsByCategoryDto {
    private Category category;
    private double totalmount;

    IncomeAnalyticsByCategoryDto(String categoryString, double totalAmount) {
        this.totalmount = totalAmount;
        this.category = Category.valueOf(categoryString);
    }

}
