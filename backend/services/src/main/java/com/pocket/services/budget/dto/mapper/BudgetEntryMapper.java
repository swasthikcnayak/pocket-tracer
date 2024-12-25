package com.pocket.services.budget.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pocket.services.budget.dto.request.BudgetEntryDto;
import com.pocket.services.budget.dto.response.BudgetEntryResponseDto;
import com.pocket.services.budget.model.BudgetEntry;

@Mapper(componentModel = "spring")
public interface BudgetEntryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budget", ignore = true)
    BudgetEntry toBudgetEntryModel(BudgetEntryDto budgetEntryDto);


    BudgetEntryResponseDto toBudgetEntryResponseDto(BudgetEntry budgetEntry);
}
