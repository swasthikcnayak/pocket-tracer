package com.pocket.services.income.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.income.dto.request.IncomeDto;
import com.pocket.services.income.service.IncomeService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/v1/income")
public class IncomeController {

    @Autowired
    IncomeService incomeService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addIncome(@Valid @RequestBody IncomeDto incomeDto,
            @AuthenticationPrincipal UserInfo userInfo) {
        return incomeService.addIncome(incomeDto, userInfo);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIncome(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo userInfo) {
        Pageable pageable = PageRequest.of(page, size);
        return incomeService.getIncome(userInfo, pageable);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateIncome(@PathVariable Long id, @Valid @RequestBody IncomeDto incomeDto,
            @AuthenticationPrincipal UserInfo userInfo) throws Exception {
        return incomeService.updateIncome(id, incomeDto, userInfo);
    }
}
