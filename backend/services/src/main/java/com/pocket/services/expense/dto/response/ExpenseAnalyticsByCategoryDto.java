package com.pocket.services.expense.dto.response;

import com.pocket.services.expense.model.Category;

import lombok.Data;

@Data
public class ExpenseAnalyticsByCategoryDto {
    private Category category;
    private double totalAmount;

    ExpenseAnalyticsByCategoryDto(String categoryString, double totalAmount) {
        this.totalAmount = totalAmount;
        this.category = Category.valueOf(categoryString);
    }

}
