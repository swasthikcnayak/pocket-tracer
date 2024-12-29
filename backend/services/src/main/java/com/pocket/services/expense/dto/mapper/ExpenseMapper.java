package com.pocket.services.expense.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pocket.services.expense.dto.request.ExpenseDto;
import com.pocket.services.expense.dto.response.ExpenseDtoResponse;
import com.pocket.services.expense.model.Expense;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Expense toExpenseModel(ExpenseDto incomeDto);

    ExpenseDtoResponse toExpenseDtoResponse(Expense e);
}
