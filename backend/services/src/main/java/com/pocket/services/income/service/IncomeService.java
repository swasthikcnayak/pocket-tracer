package com.pocket.services.income.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.pocket.services.expense.exceptions.ExpenseServiceException;
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

    public ResponseEntity<?> addIncome(IncomeDto incomeDto, UserInfo userInfo) {
        Income income = incomeMapper.toIncomeModel(incomeDto);
        income.setUser(new User(userInfo.getId()));
        try {
            incomeRepository.save(income);
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
        }
        catch (Exception e) {
            logger.error("Exception in get income by id", e);
            throw new UnhandledException(ErrorCode.INCOME_GET_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> updateIncome(Long id, IncomeDto incomeDto, UserInfo userInfo) {
        Income existingIncome = incomeRepository.findIncomeByIdAndUser(id, new User(userInfo.getId()))
                .orElseThrow(() -> new ExpenseServiceException(ErrorCode.INCOME_NOT_FOUND, "Income not found",
                        HttpStatus.NOT_FOUND));
        existingIncome.setAmount(incomeDto.getAmount());
        existingIncome.setCategory(incomeDto.getCategory());
        existingIncome.setDate(incomeDto.getDate());
        existingIncome.setTitle(incomeDto.getTitle());
        existingIncome.setDescription(incomeDto.getDescription());
        try {
            incomeRepository.save(existingIncome);
            return ResponseEntity.status(HttpStatus.CREATED).body("Updated");
        } catch (Exception e) {
            logger.error("Exception in update income by id", e);
            throw new UnhandledException(ErrorCode.INCOME_UPDATE_EXCEPTION, e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteIncome(Long id, UserInfo userInfo) {
        int result = incomeRepository.deleteByIdAndUser(id, new User(userInfo.getId()));
        if (result == 1) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        throw new ExpenseServiceException(ErrorCode.INCOME_NOT_FOUND, "Income not found", HttpStatus.NOT_FOUND);
    }
}
