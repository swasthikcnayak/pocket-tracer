package com.pocket.services.income.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.pocket.services.expense.dto.response.ExpenseDtoResponse;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.income.dto.internal.Task;
import com.pocket.services.income.exceptions.IncomeServiceException;
import com.pocket.services.income.dto.mapper.IncomeMapper;
import com.pocket.services.income.dto.request.IncomeDto;
import com.pocket.services.income.dto.response.IncomeDtoResponse;
import com.pocket.services.income.exceptions.ErrorCode;
import com.pocket.services.income.model.Income;
import com.pocket.services.income.repository.IncomeRepository;

@Service
public class IncomeService {

    private final Logger logger = LoggerFactory.getLogger(IncomeService.class);

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private IncomeAnalyticsService incomeAnalyticsService;

    public ResponseEntity<?> addIncome(IncomeDto incomeDto, UserInfo userInfo) {
        Income income = incomeMapper.toIncomeModel(incomeDto);
        income.setUser(new User(userInfo.getId()));
        try {
            incomeRepository.save(income);
            incomeAnalyticsService.handleIncome(Task.CREATE, Arrays.asList(income));
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (DataIntegrityViolationException ex) {
            throw new IncomeServiceException(ErrorCode.INCOME_CONSTRAINTS_DOES_NOT_MATCH, "Income creation error");
        } catch (JpaSystemException ex) {
            throw new IncomeServiceException(ErrorCode.INCOME_ALREADY_EXIST, "Income creation error");
        } catch (Exception e) {
            logger.error("Exception in register User", e);
            throw new UnhandledException(ErrorCode.INCOME_CREATION_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> getIncome(UserInfo userInfo, Pageable pageable) {
        if (pageable == null) {
            throw new IncomeServiceException(ErrorCode.INCOME_INVALID_PAGE, "Invalid page request");
        }
        Page<IncomeDtoResponse> incomes = incomeRepository.findAllByUser(new User(userInfo.getId()), pageable);
        return ResponseEntity.ok().body(incomes);
    }

    public ResponseEntity<?> getIncomeById(Long id, UserInfo userInfo) {
        try {
            IncomeDtoResponse expense = incomeRepository.findByIdAndUser(id, new User(userInfo.getId()))
                    .orElseThrow(() -> new IncomeServiceException(ErrorCode.INCOME_NOT_FOUND, "Income not found",
                            HttpStatus.NOT_FOUND));
            return ResponseEntity.ok().body(expense);
        } catch (IncomeServiceException e) {
            logger.error("Exception in get income by id", e);
            throw e;
        } catch (Exception e) {
            logger.error("Exception in get income by id", e);
            throw new UnhandledException(ErrorCode.INCOME_GET_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> updateIncome(Long id, IncomeDto incomeDto, UserInfo userInfo) {
        Income existingIncome = incomeRepository.findIncomeByIdAndUser(id, new User(userInfo.getId()))
                .orElseThrow(() -> new IncomeServiceException(ErrorCode.INCOME_NOT_FOUND, "Income not found",
                        HttpStatus.NOT_FOUND));
        Income oldCopy = new Income();
        BeanUtils.copyProperties(existingIncome, oldCopy);
        existingIncome.setAmount(incomeDto.getAmount());
        existingIncome.setCategory(incomeDto.getCategory());
        existingIncome.setDate(incomeDto.getDate());
        existingIncome.setTitle(incomeDto.getTitle());
        existingIncome.setDescription(incomeDto.getDescription());
        try {
            incomeRepository.save(existingIncome);
            incomeAnalyticsService.handleIncome(Task.UPDATE, Arrays.asList(oldCopy, existingIncome));
            return ResponseEntity.status(HttpStatus.CREATED).body("Updated");
        } catch (Exception e) {
            logger.error("Exception in update income by id", e);
            throw new UnhandledException(ErrorCode.INCOME_UPDATE_EXCEPTION, e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteIncome(Long id, UserInfo userInfo) {
        Income expense = incomeRepository.findIncomeByIdAndUser(id, new User(userInfo.getId()))
                .orElseThrow(() -> new IncomeServiceException(ErrorCode.INCOME_NOT_FOUND, "Expense not found",
                        HttpStatus.NOT_FOUND));
        try {
            incomeRepository.delete(expense);
            incomeAnalyticsService.handleIncome(Task.DELETE, Arrays.asList(expense));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (IncomeServiceException e) {
            logger.error("Exception in delete income by id", e);
            throw e;
        } catch (Exception e) {
            logger.error("Exception in update expense by id", e);
            throw new UnhandledException(ErrorCode.INCOME_DELETE_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> getIncomeByMonth(int month, int year, UserInfo userInfo) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);
            List<Income> income = incomeRepository
                    .findIncomeByUserAndDateTimeBetween(new User(userInfo.getId()), start, end);
             List<IncomeDtoResponse> response = new ArrayList<>();
            for (Income e : income) {
                response.add(incomeMapper.toExpenseDtoResponse(e));
            }
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            logger.error("Exception in get income by month", e);
            throw new UnhandledException(ErrorCode.INCOME_GET_BY_MONTH_EXCEPTION, e.getMessage());
        }
    }

}
