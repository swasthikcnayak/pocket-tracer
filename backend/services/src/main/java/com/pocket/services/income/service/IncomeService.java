package com.pocket.services.income.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import com.pocket.services.income.dto.request.IncomeDto;
import com.pocket.services.security.dto.UserInfo;


@Service
public class IncomeService {

    public ResponseEntity<?> addIncome(UserInfo userInfo, IncomeDto incomeDto) {
        throw new UnsupportedOperationException("Unimplemented method 'addIncome'");
    }

    public ResponseEntity<?> updateIncome(Long id, IncomeDto incomeDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateIncome'");
    }

    public ResponseEntity<?> getIncome(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIncome'");
    }
    
}
