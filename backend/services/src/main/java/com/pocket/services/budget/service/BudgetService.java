package com.pocket.services.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pocket.services.budget.dto.mapper.BudgetMapper;
import com.pocket.services.budget.dto.request.BudgetDto;
import com.pocket.services.budget.dto.response.BudgetResponseDto;
import com.pocket.services.budget.dto.response.BudgetSuccessReponse;
import com.pocket.services.budget.model.Budget;
import com.pocket.services.budget.repository.BudgetRepository;
import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.common.user.model.User;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    public ResponseEntity<?> addBudget(BudgetDto budgetDto, UserInfo userInfo) {
        Budget budget = budgetMapper.toBudgetModel(budgetDto);
        budget.setUser(new User(userInfo.getId()));
        budget.getBudgetEntries().forEach(entry -> entry.setBudget(budget));
        budgetRepository.save(budget);
        return ResponseEntity.ok()
                .body(new BudgetSuccessReponse(HttpStatus.CREATED.value(), "Budget created successfully"));
    }

    public ResponseEntity<?> getBudget(UserInfo userInfo, int month, int year) throws Exception {
        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(new User(userInfo.getId()), month, year)
                .orElseThrow(() -> new Exception(String.format("EXPENSE_NOT_FOUND")));
        BudgetResponseDto budgetResponseDto = budgetMapper.toBudgetEntryResponseDto(budget);
        return ResponseEntity.ok()
                .body(budgetResponseDto);
    }
}
