package com.pocket.services.income.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.common.user.model.User;
import com.pocket.services.income.dto.mapper.IncomeMapper;
import com.pocket.services.income.dto.request.IncomeDto;
import com.pocket.services.income.dto.response.IncomeDtoResponse;
import com.pocket.services.income.dto.response.IncomeSuccessReponse;
import com.pocket.services.income.model.Income;
import com.pocket.services.income.repository.IncomeRepository;

@Service
public class IncomeService {

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private IncomeRepository incomeRepository;

    public ResponseEntity<?> addIncome(IncomeDto incomeDto, UserInfo userInfo) {
        Income income = incomeMapper.toIncomeModel(incomeDto);
        income.setUser(new User(userInfo.getId()));
        incomeRepository.save(income);
        return ResponseEntity.ok()
                .body(new IncomeSuccessReponse(HttpStatus.CREATED.value(), "Income added successfully"));
    }

    public ResponseEntity<?> updateIncome(Long id, IncomeDto incomeDto, UserInfo userInfo) throws Exception {
        Income existingIncome = incomeRepository.findByIdAndUser(id, new User(userInfo.getId()))
                .orElseThrow(() -> new Exception(String.format("INCOME_NOT_FOUND")));
        existingIncome.setAmount(incomeDto.getAmount());
        existingIncome.setCategory(incomeDto.getCategory());
        existingIncome.setDate(incomeDto.getDate());
        existingIncome.setTitle(incomeDto.getTitle());
        existingIncome.setDescription(incomeDto.getDescription());
        incomeRepository.save(existingIncome);
        return ResponseEntity.ok()
                .body(new IncomeSuccessReponse(HttpStatus.CREATED.value(), "Income updated successfully"));
    }

    public ResponseEntity<?> getIncome(UserInfo userInfo, Pageable pageable) {
        Page<IncomeDtoResponse> incomes =  incomeRepository.findAllByUser(new User(userInfo.getId()), pageable);
        return ResponseEntity.ok().body(incomes);
    }

}
