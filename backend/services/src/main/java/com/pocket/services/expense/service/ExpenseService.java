package com.pocket.services.expense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pocket.services.expense.dto.mapper.ExpenseMapper;
import com.pocket.services.expense.dto.request.ExpenseDto;
import com.pocket.services.expense.dto.response.ExpenseDtoResponse;
import com.pocket.services.expense.dto.response.ExpenseSuccessReponse;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.repository.ExpenseRepository;
import com.pocket.services.security.dto.UserInfo;
import com.pocket.services.user.model.User;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseMapper expenseMapper;

    @Autowired
    private ExpenseRepository expenseRepository;

    public ResponseEntity<?> addExpense(ExpenseDto expenseDto, UserInfo userInfo) {
        Expense expense = expenseMapper.toExpenseModel(expenseDto);
        expense.setUser(new User(userInfo.getId()));
        expenseRepository.save(expense);
        return ResponseEntity.ok()
                .body(new ExpenseSuccessReponse(HttpStatus.CREATED.value(), "Expense added successfully"));
    }

    public ResponseEntity<?> updateExpense(Long id, ExpenseDto expenseDto, UserInfo userInfo) {
        Expense existingExpense = expenseRepository.findByIdAndUser(id, new User(userInfo.getId()))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("EXPENSE_NOT_FOUND")));
        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setCategory(expenseDto.getCategory());
        existingExpense.setDate(expenseDto.getDate());
        existingExpense.setTitle(expenseDto.getTitle());
        existingExpense.setDescription(expenseDto.getDescription());
        expenseRepository.save(existingExpense);
        return ResponseEntity.ok()
                .body(new ExpenseSuccessReponse(HttpStatus.CREATED.value(), "Expense updated successfully"));
    }

    public ResponseEntity<?> getExpense(UserInfo userInfo, Pageable pageable) {
        Page<ExpenseDtoResponse> incomes = expenseRepository.findAllByUser(new User(userInfo.getId()), pageable);
        return ResponseEntity.ok().body(incomes);
    }

}
