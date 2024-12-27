package com.pocket.services.expense.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocket.services.common.exceptions.UnhandledException;
import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.common.user.model.User;
import com.pocket.services.expense.dto.internal.Task;
import com.pocket.services.expense.dto.mapper.ExpenseMapper;
import com.pocket.services.expense.dto.request.ExpenseDto;
import com.pocket.services.expense.dto.response.ExpenseDtoResponse;
import com.pocket.services.expense.exceptions.ErrorCode;
import com.pocket.services.expense.exceptions.ExpenseServiceException;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.repository.ExpenseRepository;

@Service
public class ExpenseService {

    private final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    @Autowired
    private ExpenseMapper expenseMapper;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseAnalyticsService expenseAnalyticsService;

    public ResponseEntity<?> addExpense(ExpenseDto expenseDto, UserInfo userInfo) {
        Expense expense = expenseMapper.toExpenseModel(expenseDto);
        expense.setUser(new User(userInfo.getId()));
        try {
            expense = expenseRepository.save(expense);
            expenseAnalyticsService.handleExpense(expense, Task.CREATE);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (DataIntegrityViolationException ex) {
            throw new ExpenseServiceException(ErrorCode.EXPENSE_CONSTRAINTS_DOES_NOT_MATCH, "Expense creation error");
        } catch (JpaSystemException ex) {
            throw new ExpenseServiceException(ErrorCode.EXPENSE_ALREADY_EXIST, "Expense creation error");
        } catch (Exception e) {
            logger.error("Exception in add expense", e);
            throw new UnhandledException(ErrorCode.EXPENSE_CREATION_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> getExpense(UserInfo userInfo, Pageable pageable) {
        if (pageable == null) {
            throw new ExpenseServiceException(ErrorCode.EXPENSE_INVALID_PAGE, "Invalid page request");
        }
        Page<ExpenseDtoResponse> expense = expenseRepository.findAllByUser(new User(userInfo.getId()), pageable);
        return ResponseEntity.ok().body(expense);
    }

    public ResponseEntity<?> getExpenseById(Long id, UserInfo userInfo) {
        try {
            ExpenseDtoResponse expense = expenseRepository.findByIdAndUser(id, new User(userInfo.getId()))
                    .orElseThrow(() -> new ExpenseServiceException(ErrorCode.EXPENSE_NOT_FOUND, "Expense not found",
                            HttpStatus.NOT_FOUND));
            return ResponseEntity.ok().body(expense);
        } catch (Exception e) {
            logger.error("Exception in get expense by id", e);
            throw new UnhandledException(ErrorCode.EXPENSE_GET_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> updateExpense(Long id, ExpenseDto expenseDto, UserInfo userInfo) {
        Expense existingExpense = expenseRepository.findExpenseByIdAndUser(id, new User(userInfo.getId()))
                .orElseThrow(() -> new ExpenseServiceException(ErrorCode.EXPENSE_NOT_FOUND, "Expense not found",
                        HttpStatus.NOT_FOUND));
        Expense oldCopy = new Expense();
        BeanUtils.copyProperties(existingExpense, oldCopy);
        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setCategory(expenseDto.getCategory());
        existingExpense.setDate(expenseDto.getDate());
        existingExpense.setTitle(expenseDto.getTitle());
        existingExpense.setDescription(expenseDto.getDescription());
        try {
            existingExpense = expenseRepository.save(existingExpense);
            expenseAnalyticsService.handleExpense(oldCopy, Task.DELETE);
            expenseAnalyticsService.handleExpense(existingExpense, Task.CREATE);
            return ResponseEntity.status(HttpStatus.CREATED).body("Updated");
        } catch (Exception e) {
            logger.error("Exception in update expense by id", e);
            throw new UnhandledException(ErrorCode.EXPENSE_UPDATE_EXCEPTION, e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteExpense(Long id, UserInfo userInfo) {
        Expense expense = expenseRepository.findExpenseByIdAndUser(id, new User(userInfo.getId()))
                .orElseThrow(() -> new ExpenseServiceException(ErrorCode.EXPENSE_NOT_FOUND, "Expense not found",
                        HttpStatus.NOT_FOUND));
        try {
            expenseRepository.delete(expense);
            expenseAnalyticsService.handleExpense(expense, Task.DELETE);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("Exception in update expense by id", e);
            throw new UnhandledException(ErrorCode.EXPENSE_DELETE_EXCEPTION, e.getMessage());
        }

    }

}
