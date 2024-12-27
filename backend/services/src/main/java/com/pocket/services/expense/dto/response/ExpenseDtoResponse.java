package com.pocket.services.expense.dto.response;

import java.time.LocalDateTime;

import com.pocket.services.expense.model.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ExpenseDtoResponse {
    private Long id = -1L;
    private double amount;
    private String title;
    private Category category;
    private LocalDateTime date;
    private String description;
}
