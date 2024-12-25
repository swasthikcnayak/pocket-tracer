package com.pocket.services.income.dto.response;

import java.time.LocalDate;

import com.pocket.services.income.model.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class IncomeDtoResponse {
    private Long id = -1L;
    private double amount;
    private String title;
    private Category category;
    private LocalDate date;
    private String description;
}
