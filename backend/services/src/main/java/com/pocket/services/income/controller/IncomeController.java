package com.pocket.services.income.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.services.income.dto.request.IncomeDto;
import com.pocket.services.income.service.IncomeService;
import com.pocket.services.security.dto.UserInfo;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/v1/income")
public class IncomeController {

    @Autowired
    IncomeService incomeService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addIncome(@Valid @RequestBody IncomeDto incomeDto,@AuthenticationPrincipal UserInfo userInfo) {
        return incomeService.addIncome(userInfo, incomeDto);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIncome(Pageable pageable) {
        return incomeService.getIncome(pageable);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateIncome(@PathVariable Long id, @Valid @RequestBody IncomeDto incomeDto) {
        return incomeService.updateIncome(id, incomeDto);
    }
}
