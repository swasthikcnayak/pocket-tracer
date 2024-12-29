package com.pocket.services.budget.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pocket.services.budget.dto.request.BudgetDto;
import com.pocket.services.budget.dto.response.BudgetResponseDto;
import com.pocket.services.budget.model.Budget;

@Mapper(componentModel = "spring", uses = BudgetEntryMapper.class)
public interface BudgetMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Budget toBudgetModel(BudgetDto budgetDto);

    BudgetResponseDto toBudgetEntryResponseDto(Budget budget);
}
