package com.pocket.services.income.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pocket.services.income.dto.request.IncomeDto;
import com.pocket.services.income.dto.response.IncomeDtoResponse;
import com.pocket.services.income.model.Income;

@Mapper(componentModel = "spring")
public interface IncomeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Income toIncomeModel(IncomeDto incomeDto);

    IncomeDtoResponse toExpenseDtoResponse(Income e);
}
