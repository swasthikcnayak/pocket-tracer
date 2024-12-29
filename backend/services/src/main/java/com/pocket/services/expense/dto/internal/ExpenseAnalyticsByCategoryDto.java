package com.pocket.services.expense.dto.internal;

import com.pocket.services.expense.model.Category;

import lombok.Data;

@Data
public class ExpenseAnalyticsByCategoryDto {
    private Category category;
    private double totalmount;

    ExpenseAnalyticsByCategoryDto(String categoryString, double totalAmount) {
        this.totalmount = totalAmount;
        this.category = Category.valueOf(categoryString);
    }

}
