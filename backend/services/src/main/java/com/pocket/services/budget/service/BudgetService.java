package com.pocket.services.budget.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocket.services.budget.dto.mapper.BudgetEntryMapper;
import com.pocket.services.budget.dto.mapper.BudgetMapper;
import com.pocket.services.budget.dto.request.BudgetDto;
import com.pocket.services.budget.dto.request.BudgetEntryDto;
import com.pocket.services.budget.dto.response.BudgetResponseDto;
import com.pocket.services.budget.exceptions.BudgetServiceException;
import com.pocket.services.budget.exceptions.ErrorCode;
import com.pocket.services.budget.model.Budget;
import com.pocket.services.budget.model.BudgetEntry;
import com.pocket.services.budget.repository.BudgetEntryRepository;
import com.pocket.services.budget.repository.BudgetRepository;
import com.pocket.services.common.exceptions.UnhandledException;
import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.common.user.model.User;

@Service
public class BudgetService {

    private final Logger logger = LoggerFactory.getLogger(BudgetService.class);

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetEntryRepository budgetEntryRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private BudgetEntryMapper budgetEntryMapper;

    @Transactional
    public ResponseEntity<?> addBudget(BudgetDto budgetDto, UserInfo userInfo) {
        Budget budget = budgetMapper.toBudgetModel(budgetDto);
        budget.setUser(new User(userInfo.getId()));
        budget.getBudgetEntries().forEach(entry -> entry.setBudget(budget));
        try {
            budgetRepository.save(budget);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (DataIntegrityViolationException ex) {
            throw new BudgetServiceException(ErrorCode.BUDGET_CONSTRAINTS_DOES_NOT_MATCH, "Budget creation error");
        } catch (JpaSystemException ex) {
            throw new BudgetServiceException(ErrorCode.BUDGET_ALREADY_EXIST, "Budget creation error");
        } catch (Exception e) {
            logger.error("Exception in add budget", e);
            throw new UnhandledException(ErrorCode.BUDGET_CREATION_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> getBudget(UserInfo userInfo, int month, int year) throws Exception {
        try {
            Budget budget = budgetRepository
                    .findByUserAndMonthAndYear(new User(userInfo.getId()), month, year)
                    .orElseThrow(() -> new BudgetServiceException(ErrorCode.BUDGET_NOT_FOUND, "Budget not found",
                            HttpStatus.NOT_FOUND));
            BudgetResponseDto budgetResponseDto = budgetMapper.toBudgetEntryResponseDto(budget);
            return ResponseEntity.ok()
                    .body(budgetResponseDto);
        } catch (Exception e) {
            logger.error("Exception in get budget ", e);
            throw new UnhandledException(ErrorCode.BUDGET_GET_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> getBudgetById(UserInfo userInfo, Long id) {
        try {
            Budget budget = budgetRepository
                    .findByUserAndId(new User(userInfo.getId()), id)
                    .orElseThrow(() -> new BudgetServiceException(ErrorCode.BUDGET_NOT_FOUND, "Budget not found",
                            HttpStatus.NOT_FOUND));
            BudgetResponseDto budgetResponseDto = budgetMapper.toBudgetEntryResponseDto(budget);
            return ResponseEntity.ok()
                    .body(budgetResponseDto);
        } catch (Exception e) {
            logger.error("Exception in get budget by id", e);
            throw new UnhandledException(ErrorCode.BUDGET_GET_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> updateBudget(Long id, BudgetDto budgetDto, UserInfo userInfo) {
        try {
            Budget budget = budgetRepository
                    .findByUserAndId(new User(userInfo.getId()), id)
                    .orElseThrow(() -> new BudgetServiceException(ErrorCode.BUDGET_NOT_FOUND, "Budget not found",
                            HttpStatus.NOT_FOUND));
            List<BudgetEntry> entries = budget.getBudgetEntries();
            int counter = 0;
            for (int i = 0; i < entries.size(); i++) {
                BudgetEntry existingEntry = entries.get(i);
                if (counter == budgetDto.getBudgetEntries().size()) {
                    budgetEntryRepository.delete(existingEntry);
                    continue;
                }
                BudgetEntryDto newEntryDto = budgetDto.getBudgetEntries().get(counter);
                existingEntry.setCategory(newEntryDto.getCategory());
                existingEntry.setAmount(newEntryDto.getAmount());
                budgetEntryRepository.save(existingEntry);
                counter++;
            }

            if (counter < budgetDto.getBudgetEntries().size()) {
                for (int i = counter; i < budgetDto.getBudgetEntries().size(); i++) {
                    BudgetEntryDto newEntryDto = budgetDto.getBudgetEntries().get(i);
                    BudgetEntry newEntry = budgetEntryMapper.toBudgetEntryModel(newEntryDto);
                    newEntry.setBudget(budget);
                    budgetEntryRepository.save(newEntry);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Updated");
        } catch (Exception e) {
            logger.error("Exception in update budget", e);
            throw new UnhandledException(ErrorCode.BUDGET_UPDATE_EXCEPTION, e.getMessage());
        }
                
    }
}
